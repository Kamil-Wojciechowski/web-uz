package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.request.AddressRequest;
import com.uz.shop.animal.world.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
 */
@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@Tag(name = "Adresy")
@RequestMapping("api/v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */
    @Operation(description = "Listowanie wszystkich adresów, zależnie od zalogowanego użytkownika", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Address.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200") })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Address>> getAddress() { return addressService.findByUser(); }

    @Operation(description = "Tworzenie adresu dla użytkownika", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Address.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjectNode> postAddress(@Valid @RequestBody AddressRequest request) { return addressService.createAddress(request); }

    @Operation(description = "Pobieranie pojedynczego adresu", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Address.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/{addressId}")
    public ResponseEntity<ObjectNode> getAddress(@Parameter(description = "Identyfikator adresu", required = true) @PathVariable Long addressId) { return addressService.getAddressById(addressId); }

    @Operation(description = "Aktualizacja pojedynczego adresu", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Address.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PatchMapping("/{addressId}")
    public ResponseEntity<ObjectNode> patchAddress(@Parameter(description = "Identyfikator adresu", required = true) @PathVariable("addressId") Long addressId, @Valid @RequestBody AddressRequest request) { return addressService.updateAddress(addressId, request); }

    @Operation(description = "Usuwanie pojedynczego adresu", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Address.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "204", description = "Usuwanie powiodło się"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @DeleteMapping("/{addressId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAddress(@Parameter(description = "Identyfikator adresu", required = true) @PathVariable("addressId") Long addressId) { addressService.deleteAddress(addressId); }
}
