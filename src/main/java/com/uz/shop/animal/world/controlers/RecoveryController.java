package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.models.Token;
import com.uz.shop.animal.world.request.RecoveryRequest;
import com.uz.shop.animal.world.services.RecoveryService;
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
@RequestMapping(path = "api/v1/recovery")
@Tag(name = "Odzyskiwanie")
@AllArgsConstructor
public class RecoveryController {
    private final RecoveryService recoveryService;

    /*
GetMapping - Request GET - Zbieranie informacji - 200
PostMapping - Request POST - Tworzenie elementów - 201 / 400
PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
 */

    @Operation(description = "Odzyskiwanie konta.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PostMapping("/{email}")
    public ResponseEntity<ObjectNode> recovery(@Parameter(description = "Email", required = true) @PathVariable("email") String email) {
        return recoveryService.recovery(email);
    }

    @Operation(description = "Sprawdzanie tokenu.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/token/{token}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void getToken(@Parameter(description = "Token", required = true) @PathVariable("token") String token) {
        recoveryService.getToken(token);
    }

    @Operation(description = "Potwierdzanie konta.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PostMapping("/token/{token}")
    public ResponseEntity<ObjectNode> confirm(@Parameter(description = "Token", required = true) @PathVariable("token") String token, @Valid @RequestBody RecoveryRequest recoveryRequest) {
        return recoveryService.confirmToken(token, recoveryRequest);
    }

}
