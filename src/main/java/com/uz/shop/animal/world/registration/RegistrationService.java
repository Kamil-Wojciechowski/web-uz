package com.uz.shop.animal.world.registration;

import com.uz.shop.animal.world.user.User;
import com.uz.shop.animal.world.user.UserService;
import com.uz.shop.animal.world.user.UserType;
import com.uz.shop.animal.world.email.EmailSender;
import com.uz.shop.animal.world.registration.token.Token;
import com.uz.shop.animal.world.registration.token.TokenService;
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
    public static final String TOKEN_EXPIRED = "Token expired!";
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, WRONG_FORMAT_EMAIL);
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

        if(tokenItem.getConfirmedAt() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, TOKEN_EXPIRED);
        }

        tokenService.setConfirmedAt(token);
        userService.enableUser(tokenItem.getUser().getEmail());
        return "Confirmed";
    }


}
