package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.models.enums.TokenType;
import com.uz.shop.animal.world.request.RecoveryRequest;
import com.uz.shop.animal.world.services.email.EmailSender;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import com.uz.shop.animal.world.validators.PasswordValidator;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;

import static com.uz.shop.animal.world.utils.Dictionary.*;

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy
@Service
@AllArgsConstructor
public class RecoveryService {
    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final PasswordValidator passwordValidator;
    @Autowired
    private final EmailSender emailSender;
    @Autowired
    private final ObjectMapper mapper;

    /*
    Zapytanie o recovery
    Użytkownik zostaje wyszukany po emailu.
    Pobierany jest nowy token.
    Następnie wysyłany.
    Zwracany jest wcześniej przygotowany response.
     */
    public ResponseEntity<ObjectNode> recovery(String email) {
        User user = userService.getUserByEmail(email);

        String token = userService.recoveryUser(user);

        emailSender.send(user.getEmail(), token, false);

        return recoveryResponse();
    }

    //Przygotowany response od serwera
    private ResponseEntity<ObjectNode> recoveryResponse() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", RECOVERY_EMAIL);
        objectNode.put("successful", true);
        return ResponseEntity.status(HttpStatus.OK).body(objectNode);
    }

    /*
    Potwierdzanie Tokena recovery
    Pobierany zostaje token z bazy, walidowany względem czasu wygaśniecia.
    Walidowany jest body pod względem takich samych haseł.
    Hasło zostaje encryptowane oraz zmieniane użytkownikowi.
    Zwracany jest wcześniej przygotowany response.
     */
    @Transactional
    public ResponseEntity<ObjectNode> confirmToken(String token, RecoveryRequest recoveryRequest) {
        Token tokenItem = tokenService.getToken(token)
                .orElseThrow(() -> new RestClientResponseException(TOKEN_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null));


        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            tokenService.deleteToken(tokenItem);
            return ErrorResponseCreator.buildBadRequest("Error", TOKEN_EXPIRED);
        }

        if(!passwordValidator.test(recoveryRequest.getPassword(), recoveryRequest.getConfirmedPassword())) {
            return ErrorResponseCreator.buildBadRequest("Error", PASSWORD_ARE_NOT_THE_SAME);
        }

        User user = tokenItem.getUser();
        String encodedPassword = bCryptPasswordEncoder.encode(recoveryRequest.getPassword());

        userService.changePassword(encodedPassword, user.getId());
        tokenService.deleteToken(tokenItem);

        return confirmResponse();
    }

    //Przygotowany response od serwera
    private ResponseEntity<ObjectNode> confirmResponse() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", PASSWORD_CHANGED_SUCCESSFULLY);
        objectNode.put("successful", true);
        return ResponseEntity.ok(objectNode);
    }


    //Sprawdzanie, czy token istnieje i jest poprawny
    public void getToken(String token) {
        Token tokenItem = tokenService.getToken(token)
                .orElseThrow(() -> new RestClientResponseException(TOKEN_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null));

        LocalDateTime expiredAt = tokenItem.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            tokenService.deleteToken(tokenItem);
            throw new RestClientResponseException(TOKEN_EXPIRED, 400, HttpStatus.BAD_REQUEST.name(), null, null, null);
        }

        if(tokenItem.getTokenType() != TokenType.RECOVERY) {
            throw new RestClientResponseException(TOKEN_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null);
        }
    }
}
