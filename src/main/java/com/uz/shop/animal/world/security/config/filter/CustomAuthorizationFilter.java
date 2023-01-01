package com.uz.shop.animal.world.security.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.AuthorizationService;
import com.uz.shop.animal.world.services.UserService;
import com.uz.shop.animal.world.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/*
Filtr odpowiadający za autoryzacje, jest on wykonywany raz na zapytanie
 */
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService user;

    @Autowired
    private JwtUtil jwtUtil;

    // Owe ścieżki nie powinny być sprawdzane przed zapytaniem
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/api/v*/login", request.getServletPath()) ||
                new AntPathMatcher().match("/api/v*/register/**", request.getServletPath()) ||
                new AntPathMatcher().match("/api/v*/token/refresh/**", request.getServletPath()) ||
                new AntPathMatcher().match("/api/v*/register/**", request.getServletPath()) ||
                new AntPathMatcher().match("/api/v*/recovery/**", request.getServletPath());
    }


    //Sprawdza czy istnieje token w headerze Authentication, Wypisuje odpowiednią informację, a następnie sprawdza użytkownika
    // lub przechodzi dalej do filtrowania
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String requestTokenHeader = request.getHeader("Authorization");

       String username = null;
       String jwtToken = null;

       if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
           jwtToken = requestTokenHeader.substring(7);
           try {
               username = jwtUtil.getUsernameFromToken(jwtToken);
           } catch (IllegalArgumentException e) {
               System.out.println("Unable to get JWT Token");
           } catch (ExpiredJwtException e) {
               System.out.println("JWT Token has expired");
           }
       } else {
           logger.warn("JWT Token does not begin with Bearer String");
       }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = user.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
