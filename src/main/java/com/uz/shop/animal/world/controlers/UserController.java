package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
*/
@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@Tag(name = "Użytkownik")
@RequestMapping("api/v1/users")
public class UserController {
    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */
    @Autowired
    private final UserService userService;

    @Operation(description = "Pobieranie informacji o użytkowniku. Dostępne tylko z autoryzacją.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping
    public User getUser() {
        return userService.getUserByAuth();
    }
}
