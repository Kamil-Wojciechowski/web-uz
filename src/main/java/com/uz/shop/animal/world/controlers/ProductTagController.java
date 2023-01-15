package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.request.ProductTagRequest;
import com.uz.shop.animal.world.services.ProductTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
*/
@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@Tag(name = "Tagi")
@RequestMapping("api/v1/products/tags")
public class ProductTagController {

    private final ProductTagService productTagService;

    /*
GetMapping - Request GET - Zbieranie informacji - 200
PostMapping - Request POST - Tworzenie elementów - 201 / 400
PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
 */

    @Operation(description = "Listowanie wszystkich tagów.", responses = {
       @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductTag.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<ProductTag>> productTags() {
        return productTagService.getAll();
    }


    @Operation(description = "Tworzenie tagu. Dostępne tylko z autoryzacją oraz odpowiednimi uprawnieniami.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ProductTag.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ObjectNode> createProductTag(@Valid @RequestBody ProductTagRequest productTag) {
        return productTagService.create(productTag);
    }

    @Operation(description = "Aktualizacja tagu. Dostępne tylko z autoryzacją oraz odpowiednimi uprawnieniami.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ProductTag.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{productTagId}")
    public ResponseEntity<ObjectNode> updateProductTag(@Parameter(description = "Identyfikator tagu", required = true) @PathVariable("productTagId") Integer productTagId, @Valid @RequestBody ProductTagRequest request) {
        return productTagService.update(productTagId, request);
    }


    @Operation(description = "Pobieranie pojedynczego tagu. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = ProductTag.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/{productTagId}")
    public ResponseEntity<ObjectNode> getProductTagById(@Parameter(description = "Identyfikator tagu", required = true) @PathVariable("productTagId") Integer productTagId) {
        return productTagService.getProductTagById(productTagId);
    }

    @Operation(description = "Usuwanie tagu. Dostępne tylko z autoryzacją i rolą admina.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ProductTag.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "204", description = "Usuwanie powiodło się"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{productTagId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductTag(@Parameter(description = "Identyfikator tagu", required = true) @PathVariable("productTagId") Integer productTagId) {
        productTagService.delete(productTagId);
    }

}
