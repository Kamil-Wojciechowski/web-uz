package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.request.ProductPostRequest;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.services.ProductService;
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
@Tag(name = "Produkty")
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    /*
GetMapping - Request GET - Zbieranie informacji - 200
PostMapping - Request POST - Tworzenie elementów - 201 / 400
PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
 */
    @Operation(description = "Listowanie wszystkich produktów, zależnie od uprawnień zalogowanego użytkownika", responses = {
        @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return productService.findAll();
    }


    @Operation(description = "Tworzenie produktu. Dostępne tylko z autoryzacją oraz odpowiednimi uprawnieniami.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ObjectNode> createProduct(@Valid @RequestBody ProductPostRequest product) {
        return productService.create(product);
    }

    @Operation(description = "Aktualizacja produktu. Dostępne tylko z autoryzacją oraz odpowiednimi uprawnieniami.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ObjectNode> patchProduct(@Parameter(description = "Identyfikator produktu", required = true) @PathVariable("productId") Long productId, @Valid @RequestBody ProductRequest request)  {
        return productService.update(productId, request);
    }

    @Operation(description = "Pobieranie pojedynczego produktu. Dostępne tylko z autoryzacją.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/{productId}")
    public ResponseEntity<ObjectNode> getProduct(@Parameter(description = "Identyfikator produktu", required = true)  @PathVariable("productId") Long productId)  {
        return productService.getProductById(productId);
    }

    @Operation(description = "Usuwanie produktu. Dostępne tylko z autoryzacją i rolą admina.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "204", description = "Usuwanie powiodło się"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@Parameter(description = "Identyfikator produktu", required = true) @PathVariable("productId") Long productId) {
        productService.delete(productId);
    }
}
