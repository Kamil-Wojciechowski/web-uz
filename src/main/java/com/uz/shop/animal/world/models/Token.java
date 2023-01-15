package com.uz.shop.animal.world.models;

import com.uz.shop.animal.world.models.enums.TokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Model Tokenu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name="tokens")
public class Token {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    //Enumerated Oznacza, że dane pole jest Enumeratorem oraz wartość, którą chcemy przechowywać jest String nie indeks
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    @NotEmpty
    private String token;

    //Flaga odpowiada za automatyczne tworzenie czasu utworzenia
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public Token(User user, TokenType tokenType, String token, LocalDateTime expiredAt) {
        this.user = user;
        this.tokenType = tokenType;
        this.token = token;
        this.expiresAt = expiredAt;
    }
}
