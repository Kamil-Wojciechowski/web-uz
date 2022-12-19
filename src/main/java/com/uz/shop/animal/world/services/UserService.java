package com.uz.shop.animal.world.services;

import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.repository.UserRepository;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.models.enums.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientResponseException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.uz.shop.animal.world.utils.Dictionary.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {


    public final static Long expirationHours = Long.valueOf(12);

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public User getUserByEmail(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(@Valid @RequestBody User user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if(userExists) {
            throw new RestClientResponseException(EMAIL_ALREADY_TAKEN, 400, HttpStatus.BAD_REQUEST.name(), null, null, null);
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        User createdUser = userRepository.save(user);

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

    public String recoveryUser(@Valid @RequestBody User user) {
        String recoveryToken = UUID.randomUUID().toString();
        Token token = new Token(
                user,
                TokenType.RECOVERY,
                recoveryToken,
                LocalDateTime.now().plusHours(expirationHours)
        );

        tokenService.saveToken(token);

        return recoveryToken;
    }

    public String createNewRegisterToken(@Valid @RequestBody User user) {
        String registrationToken = UUID.randomUUID().toString();
        Token token = new Token(
                user,
                TokenType.REGISTER,
                registrationToken,
                LocalDateTime.now().plusHours(expirationHours)
        );

        tokenService.saveToken(token);

        return registrationToken;
    }

    public int changePassword(String password, Long id) {
        return userRepository.updatePassword(password, id);
    }

    public int enableUser(String email) {
       return userRepository.enableUser(email);
    }
}
