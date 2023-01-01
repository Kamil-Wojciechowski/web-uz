package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Payment;
import com.uz.shop.animal.world.request.PaymentRequest;
import com.uz.shop.animal.world.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@RequestMapping("api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<Payment>> getPayments(@PathVariable("orderId") Long idOrder) {
        return paymentService.getAllForOrder(idOrder);
    }

    @GetMapping("/last/{orderId}")
    public ResponseEntity<Payment> getLastPayment(@PathVariable("orderId") Long idOrder) {
        return paymentService.getLastPaymentForOrder(idOrder);
    }

    @PostMapping
    public ResponseEntity<ObjectNode> postPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<ObjectNode> patchPayment(@PathVariable("paymentId") Long idPayment, @Valid @RequestBody PaymentRequest request) {
        return paymentService.updatePayment(idPayment, request);
    }
}
