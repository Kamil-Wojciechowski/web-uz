package com.uz.shop.animal.world.services;

import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.repository.UserRepository;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.models.enums.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

//Serwis odpowiadający za wszystkie procesy związane z użytkownikiem
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


    // Funkcja, która zwraca użytkownika po mailu
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    // Funkcja, która zwraca użytkownika po mailu
    public User getUserByEmail(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }


    /*
    Rejestracja użytkownika
    Sprawdza, czy użytkownik istnieje, jeśli tak zwraca error z informacją, że taki email został wykorzystany
    Następnie enkodowane jest hasło oraz ustawiane w obiekcie
    Zapisywany jest użytkownik w bazie.
    Tworzony jest token typu rejestracji.
    Po zapisanym tokenie w bazie zwracany jest owy token
     */
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

    /*
    Recovery Konta
    Tworzony jest nowy token po użytkowniu.
    Zapisujemy token w bazie
     */
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


    /*
    Tworzony jest nowy register token oraz zapisywany w bazie.
     */
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

    // Zmiana hasła
    public int changePassword(String password, Long id) {
        return userRepository.updatePassword(password, id);
    }
    //Zatwierdzanie użytkownika
    public int enableUser(String email) {
       return userRepository.enableUser(email);
    }

    //Zbieranie użytkownika po tokenie
    public User getUserByAuth() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
