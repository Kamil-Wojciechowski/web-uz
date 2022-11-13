package com.uz.shop.animal.world.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private static Algorithm algorithm;

    @Value("${server.jwt.secret}")
    public void setAlgorithm(String secret) {
        algorithm = Algorithm.HMAC512(secret.getBytes());
    }

    public static Algorithm getAlgorithm() {
        return algorithm;
    }

    private static String issuer;

    @Value("${server.jwt.issuer}")
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }


    private static Long accessExpTime;

    @Value("${server.jwt.accessExpTime}")
    public void setAccessExpTime(String accessExpTime) {
        this.accessExpTime = Long.valueOf(accessExpTime);
    }

    private static Long refreshExpTime;

    @Value("${server.jwt.refreshExpTime}")
    public void setRefreshExpTime(String refreshExpTime) {
        this.refreshExpTime = Long.valueOf(refreshExpTime);
    }

    private static String createAccessToken(User user, Date expiresAt) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    private static String createRefreshToken(User user, Date expiresAt) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }

    private static Map<String, String> mapTokensResponse(String accessToken, String refreshToken, String createdAt, String expiresAt) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("created_at", createdAt);
        tokens.put("expires_at", expiresAt);

        return tokens;
    }

    public static Map<String, String> createTokenPair(User user) {
        Date createdAt = new Date(System.currentTimeMillis());
        Date accessExpiresAt = new Date(System.currentTimeMillis() + accessExpTime);
        Date refExpiresAt = new Date(System.currentTimeMillis() + refreshExpTime);

        String accessToken = createAccessToken(user, accessExpiresAt);
        String refreshToken = createRefreshToken(user, refExpiresAt);

        Map<String, String> tokens = mapTokensResponse(accessToken, refreshToken, createdAt.toString(), accessExpiresAt.toString());
        return tokens;
    }

    public static Map<String, String> refreshToken(String refreshToken, UserService userService) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String email = decodedJWT.getSubject();
        User user = userService.getUserByEmail(email);

        Date createdAt = new Date(System.currentTimeMillis());
        Date accessExpiresAt = new Date(System.currentTimeMillis() + refreshExpTime);

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(accessExpiresAt)
                .withIssuer(issuer)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        Map<String, String> tokens = mapTokensResponse(accessToken, refreshToken, createdAt.toString(), accessExpiresAt.toString());

        return tokens;
    }
}
