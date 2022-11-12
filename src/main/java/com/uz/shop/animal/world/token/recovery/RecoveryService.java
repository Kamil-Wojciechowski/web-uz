package com.uz.shop.animal.world.token.recovery;

import com.uz.shop.animal.world.email.EmailSender;
import com.uz.shop.animal.world.security.user.User;
import com.uz.shop.animal.world.security.user.UserService;
import com.uz.shop.animal.world.token.Token;
import com.uz.shop.animal.world.token.TokenService;
import com.uz.shop.animal.world.validator.PasswordValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

import static com.uz.shop.animal.world.token.registration.RegistrationService.*;

@Service
@AllArgsConstructor
public class RecoveryService {

    private final UserService userService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordValidator passwordValidator;
    private final EmailSender emailSender;

    public String recovery(String email) {
        User user = userService.getUserByEmail(email);

        String token = userService.recoveryUser(user);

        emailSender.send(user.getEmail(), token);

        return token;
    }

    @Transactional
    public String confirmToken(String token, RecoveryRequest recoveryRequest) {
        Token tokenItem = tokenService.getToken(token)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, TOKEN_NOT_FOUND));

        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            tokenService.deleteToken(tokenItem);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, TOKEN_EXPIRED);
        }

        if(!passwordValidator.test(recoveryRequest.getConfirmedPassword(), recoveryRequest.getConfirmedPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, PASSWORD_ARE_NOT_THE_SAME);
        }

        User user = tokenItem.getUser();
        String encodedPassword = bCryptPasswordEncoder.encode(recoveryRequest.getPassword());

        userService.changePassword(encodedPassword, user.getId());
        return "Password changed";
    }
}
