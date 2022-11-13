package com.uz.shop.animal.world.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uz.shop.animal.world.security.user.User;
import com.uz.shop.animal.world.security.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorizationToken {
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

    private static UserService userService;

    private static String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpTime))
                .withIssuer(issuer)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    private static String createRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    private static Map<String, String> mapTokens(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public static Map<String, String> createTokenPair(User user) {
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);
        Map<String, String> tokens = mapTokens(accessToken, refreshToken);
        return tokens;
    }
}
