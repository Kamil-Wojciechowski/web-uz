package com.uz.shop.animal.world.appuser;

import com.uz.shop.animal.world.registration.token.Token;
import com.uz.shop.animal.world.registration.token.TokenService;
import com.uz.shop.animal.world.registration.token.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    public static final String EMAIL_ALREADY_TAKEN = "Email already taken!";
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND = "User with email %s not found!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static Long expirationHours = Long.valueOf(12);
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(User appUser) {
        boolean userExists = userRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if(userExists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_TAKEN);
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        User createdUser = userRepository.save(appUser);

        String registrationToken = UUID.randomUUID().toString();
        Token token = new Token(
                createdUser,
                TokenType.REGISTER,
                registrationToken,
                LocalDateTime.now().plusHours(expirationHours)
        );

        tokenService.saveToken(token);

        return registrationToken;
    }

    public int enableUser(String email) {
       return userRepository.enableUser(email);
    }
}
