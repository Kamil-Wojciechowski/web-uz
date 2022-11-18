package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.UserType;
import com.uz.shop.animal.world.services.email.EmailSender;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.request.RegistrationRequest;
import com.uz.shop.animal.world.validator.EmailValidator;
import com.uz.shop.animal.world.validator.PasswordValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

import static com.uz.shop.animal.world.utils.Dictionary.*;

@Service
@AllArgsConstructor
public class RegistrationService {


    @Autowired
    private final ObjectMapper mapper;
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

    public ObjectNode register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, WRONG_FORMAT_EMAIL);
        }

        if(!passwordValidator.test(request.getPassword(), request.getConfirmedPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, PASSWORD_ARE_NOT_THE_SAME);
        }

        String token = userService.signUpUser(new User(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                UserType.CUSTOMER,
                request.getPassword()
                ));

        emailSender.send(request.getEmail(), token);

        return registerResponse();
    }

    private ObjectNode registerResponse() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", EMAIL_SEND_CONFIRM);
        objectNode.put("successful", true);
        return objectNode;
    }

    @Transactional
    public ObjectNode confirmToken(String token) {
        Token tokenItem = tokenService.getToken(token)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, TOKEN_NOT_FOUND));

        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            tokenService.deleteToken(tokenItem);
            userService.createNewRegisterToken(tokenItem.getUser());
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, TOKEN_EXPIRED);
        }

        tokenService.deleteToken(tokenItem);
        userService.enableUser(tokenItem.getUser().getEmail());

        return confirmResponse();
    }

    private ObjectNode confirmResponse() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", "Email has been confirmed!");
        objectNode.put("successful", true);
        return objectNode;
    }


}
