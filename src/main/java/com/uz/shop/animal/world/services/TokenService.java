package com.uz.shop.animal.world.services;

import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

// Seriws odpowiadający za tokeny
@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    //Zapisanie tokenu w bazie
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    //Znalezienie tokenu po kluczu
    public Optional<Token> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    //Ustawienie potwierdzenia tokenu
    public int setConfirmedAt(String token) {
        return tokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    //Usuwanie tokenu
    public void deleteToken(Token token) {tokenRepository.delete(token);}
}
