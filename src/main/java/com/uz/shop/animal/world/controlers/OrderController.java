package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.request.OrderPatchRequest;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.services.OrderService;
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
@Tag(name = "Zamówienia")
@RequestMapping("api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */
    @Operation(description = "Listowanie wszystkich zamówień, zależnie od uprawnień zalogowanego użytkownika", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Order.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(){return orderService.getAll();}

    @Operation(description = "Tworzenie zamówienia dla użytkownika. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Order.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping
    public ResponseEntity<ObjectNode> postOrder(@Valid @RequestBody OrderRequest request) { return orderService.createOrder(request); }

    @Operation(description = "Aktualizacja pojedynczego zamówienia. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Order.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PatchMapping("/{orderId}")
    public ResponseEntity<ObjectNode> patchOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId, @Valid @RequestBody OrderPatchRequest request) { return orderService.updateOrder(orderId, request); }

    @Operation(description = "Pobieranie pojedynczego zamówienia. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Order.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/{orderId}")
    public ResponseEntity<ObjectNode> getOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId){return orderService.getOrderById(orderId);}

    @Operation(description = "Usuwanie pojedynczego zamówienia. Dostępne tylko z autoryzacją rolą admina.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Order.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "204", description = "Usuwanie powiodło się"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId) { orderService.deleteOrder(orderId); }


}
