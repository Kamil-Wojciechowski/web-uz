package com.uz.shop.animal.world.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.UserService;
import com.uz.shop.animal.world.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uz.shop.animal.world.utils.Dictionary.TOKEN_EXPIRED;
import static com.uz.shop.animal.world.utils.Dictionary.TOKEN_NOT_FOUND;

@Service
@AllArgsConstructor
public class AuthorizationService {
    @Autowired
    private JwtUtil jwtUtil;


    private Map<String, String> mapTokensResponse(String accessToken, String refreshToken, Date createdAt, Date expiresAt) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("created_at", createdAt.toString());
        tokens.put("expires_at", expiresAt.toString());

        return tokens;
    }

    public Map<String, String> createTokenPair(User user) {

        String accessToken =  jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Map<String, String> tokens = mapTokensResponse(accessToken, refreshToken, jwtUtil.getIssuedAtDateFromToken(accessToken), jwtUtil.getExpirationDateFromToken(accessToken));
        return tokens;
    }

    public Map<String, String> refreshToken(String refreshToken, UserDetails user) {
        if(jwtUtil.canTokenBeRefreshed(refreshToken)) {

            String accessToken = jwtUtil.generateToken(user);

            Map<String, String> tokens = mapTokensResponse(accessToken, refreshToken, jwtUtil.getIssuedAtDateFromToken(accessToken), jwtUtil.getExpirationDateFromToken(accessToken));

            return tokens;
        } else {
            throw new RestClientResponseException(TOKEN_EXPIRED, 400, HttpStatus.UNAUTHORIZED.name(), null, null, null) ;
        }
    }
}
