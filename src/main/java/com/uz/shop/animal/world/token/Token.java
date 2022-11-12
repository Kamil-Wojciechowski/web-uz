package com.uz.shop.animal.world.token;

import com.uz.shop.animal.world.security.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    @NotEmpty
    private String token;

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
