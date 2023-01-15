package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.AuthorizationService;
import com.uz.shop.animal.world.services.UserService;
import com.uz.shop.animal.world.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
*/
@RestController
@RequestMapping(path = "api/v1/token/refresh")
@Tag(name = "Odświeżanie")
@AllArgsConstructor
public class RefreshController {
    @Autowired
    private final AuthorizationService authorizationService;

    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtil jwtUtil;
    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */

    @Operation(description = "Odświeżanie tokenu autoryzacyjnego.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PostMapping("/{refreshToken}")
    public void refreshToken(@PathVariable("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
// Sprawdza czy podany token nie jest nullem, jeśli to prawda tworzyumy nowy token
        if (refreshToken != null) {
            try {
                String email = jwtUtil.getUsernameFromToken(refreshToken);

                UserDetails user = userService.loadUserByUsername(email);

                Map<String, String> tokens = authorizationService.refreshToken(refreshToken, user);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}