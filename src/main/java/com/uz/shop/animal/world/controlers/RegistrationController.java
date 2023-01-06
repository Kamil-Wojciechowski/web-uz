package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.request.RegistrationRequest;
import com.uz.shop.animal.world.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
*/
@RestController
@RequestMapping(path = "api/v1/register")
@Tag(name = "Rejestracja")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */

    @Operation(description = "Rejestracja.", responses = {
       @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
       @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping
    public ResponseEntity<ObjectNode> register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @Operation(description = "Potwierdzanie rejestracji.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping("/{token}")
    public ResponseEntity<ObjectNode> confirm(@Parameter(description = "Token", required = true) @PathVariable("token") String token) {
        return registrationService.confirmToken(token);
    }
}
