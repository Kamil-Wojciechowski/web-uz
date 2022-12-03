package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.UserType;
import com.uz.shop.animal.world.request.RegistrationRequest;
import com.uz.shop.animal.world.services.email.EmailSender;
import com.uz.shop.animal.world.utils.Dictionary;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import com.uz.shop.animal.world.validators.EmailValidator;
import com.uz.shop.animal.world.validators.PasswordValidator;
import com.uz.shop.animal.world.validators.RecaptchaValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;

import static com.uz.shop.animal.world.utils.Dictionary.*;

@Service
@AllArgsConstructor
public class RegistrationService {
    @Autowired
    private final EmailValidator emailValidator;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final EmailSender emailSender;
    @Autowired
    private final PasswordValidator passwordValidator;
    @Autowired
    private final RecaptchaValidator recaptchaValidator;

    private final ObjectNode objectNode = new ObjectMapper().createObjectNode();

    public ResponseEntity<ObjectNode> register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        boolean isRecaptchaVerified = recaptchaValidator.test(request.getRecaptchaToken());

        if(!isValidEmail) {
            return ErrorResponseCreator.buildResponse(HttpStatus.BAD_REQUEST,"Error", WRONG_FORMAT_EMAIL);
        }

//        if (!isRecaptchaVerified) {
//            return ErrorResponseCreator.buildResponse(HttpStatus.BAD_REQUEST, "Error", INVALID_RECAPTCHA);
//        }

        if(!passwordValidator.test(request.getPassword(), request.getConfirmedPassword())) {
            return ErrorResponseCreator.buildBadRequest("Error", PASSWORD_ARE_NOT_THE_SAME);
        }

        String token = userService.signUpUser(new User(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                UserType.ROLE_CUSTOMER,
                request.getPassword()
                ));

        emailSender.send(request.getEmail(), token);

        return registerResponse();
    }

    private ResponseEntity<ObjectNode> registerResponse() {
        objectNode.put("message", EMAIL_SEND_CONFIRM);
        objectNode.put("data", new ObjectMapper().createObjectNode());
        objectNode.put("successful", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(objectNode);
    }

    @Transactional
    public ResponseEntity<ObjectNode> confirmToken(String token) {
        Token tokenItem = tokenService.getToken(token)
                .orElseThrow(() -> new RestClientResponseException(TOKEN_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null));

        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            tokenService.deleteToken(tokenItem);
            userService.createNewRegisterToken(tokenItem.getUser());
            return ErrorResponseCreator.buildBadRequest("Error", TOKEN_EXPIRED);
        }

        tokenService.deleteToken(tokenItem);
        userService.enableUser(tokenItem.getUser().getEmail());

        return confirmResponse();
    }

    private ResponseEntity<ObjectNode> confirmResponse() {
        objectNode.put("message", "Email has been confirmed!");
        objectNode.put("data", new ObjectMapper().createObjectNode());
        objectNode.put("successful", true);
        return ResponseEntity.status(HttpStatus.OK).body(objectNode);
    }


}
