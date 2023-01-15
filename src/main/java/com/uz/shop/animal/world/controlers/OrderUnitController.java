package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.request.OrderUnitRequest;
import com.uz.shop.animal.world.services.OrderUnitService;
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
@Tag(name = "Linia Zamówienia")
@RequestMapping("api/v1/orders/{orderId}/units")
public class OrderUnitController {
    @Autowired
    private OrderUnitService orderUnitService;

        /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */
    @Operation(description = "Listowanie wszystkich linii zamówienia, zależnie od uprawnień zalogowanego użytkownika oraz zamówienia", responses = {
       @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderUnit.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<OrderUnit>> getOrderUnits(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId) {return orderUnitService.getAll(orderId);}

    @Operation(description = "Tworzenie linii zamówienia dla użytkownika po zamówieniu. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping
    public ResponseEntity<ObjectNode> postOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId, @Valid @RequestBody OrderUnitRequest request) { return orderUnitService.createOrderUnit(orderId, request); }

    @Operation(description = "Aktualizacja pojedynczej linii zamówienia po zamówieniu oraz linii. Dostępne tylko z autoryzacją oraz uprawnieniami.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{orderUnitId}")
    public ResponseEntity<ObjectNode> patchOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId, @Parameter(description = "Identyfikator linii zamówienia", required = true) @PathVariable("orderUnitId") Long orderUnitId, @Valid @RequestBody OrderUnitRequest request) { return orderUnitService.updateOrderUnit(orderId, orderUnitId, request); }

    @Operation(description = "Pobieranie pojedynczej linii zamówienia po zamówieniu oraz linii. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/{orderUnitId}")
    public ResponseEntity<ObjectNode> getOrderUnit(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId, @Parameter(description = "Identyfikator linii zamówienia", required = true) @PathVariable("orderUnitId") Long orderUnitId) { return orderUnitService.getByOrderUnitId(orderId, orderUnitId); }

    @Operation(description = "Usuwanie pojedynczej linii zamówienia. Dostępne tylko z autoryzacją i rolą admina.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "204", description = "Usuwanie powiodło się"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{orderUnitId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long orderId, @Parameter(description = "Identyfikator linii zamówienia", required = true) @PathVariable("orderUnitId") Long orderUnitId) { orderUnitService.deleteOrderUnit(orderId, orderUnitId); }
}
