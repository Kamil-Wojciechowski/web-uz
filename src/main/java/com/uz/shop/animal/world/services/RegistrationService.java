package com.uz.shop.animal.world.services;

import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.UserType;
import com.uz.shop.animal.world.services.email.EmailSender;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.request.RegistrationRequest;
import com.uz.shop.animal.world.validator.EmailValidator;
import com.uz.shop.animal.world.validator.PasswordValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private static final String WRONG_FORMAT_EMAIL = "Email have wrong format!";
    public static final String TOKEN_NOT_FOUND = "Token not found!";
    public static final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed";
    public static final String PASSWORD_ARE_NOT_THE_SAME = "Password are not the same!";
    public static final String TOKEN_EXPIRED = "Token expired!";
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final PasswordValidator passwordValidator;

    public String register(RegistrationRequest request) {
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

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
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
        return "Confirmed";
    }


}
