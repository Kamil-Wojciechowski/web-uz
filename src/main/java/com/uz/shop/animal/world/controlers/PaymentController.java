package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.models.Payment;
import com.uz.shop.animal.world.request.PaymentRequest;
import com.uz.shop.animal.world.services.PaymentService;
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
@Tag(name = "Płatności")
@RequestMapping("api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Operation(description = "Pobieranie wszystkich płatności w danym zamówieniu.  Dostępne tylko z autoryzacją.", responses = {
       @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Payment.class)), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")})
    @GetMapping("/{orderId}")
    public ResponseEntity<List<Payment>> getPayments(@Parameter(description = "Identyfikator zamówienia", required = true)  @PathVariable("orderId") Long idOrder) {
        return paymentService.getAllForOrder(idOrder);
    }

    @Operation(description = "Pobieranie ostatniej płatności w danym zamówieniu.  Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Payment.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/last/{orderId}")
    public ResponseEntity<Payment> getLastPayment(@Parameter(description = "Identyfikator zamówienia", required = true) @PathVariable("orderId") Long idOrder) {
        return paymentService.getLastPaymentForOrder(idOrder);
    }

    @Operation(description = "Tworzenie płatności. Dostępne tylko z autoryzacją.", responses = {
       @ApiResponse(content = @Content(schema = @Schema(implementation = Payment.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201", description = "Utworzono"),
       @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe")})
    @PostMapping
    public ResponseEntity<ObjectNode> postPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @Operation(description = "Aktualizacja płatności. Dostępne tylko z autoryzacją.", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = Payment.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200", description = "Pobrano"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "400", description = "Błędne informację wejściowe"),
        @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @PatchMapping("/{paymentId}")
    public ResponseEntity<ObjectNode> patchPayment(@PathVariable("paymentId") Long idPayment, @Valid @RequestBody PaymentRequest request) {
        return paymentService.updatePayment(idPayment, request);
    }
}
