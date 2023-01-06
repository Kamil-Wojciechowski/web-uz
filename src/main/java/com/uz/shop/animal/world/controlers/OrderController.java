package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(){return orderService.getAll();}

    @PostMapping
    public ResponseEntity<ObjectNode> postOrder(@Valid @RequestBody OrderRequest request) { return orderService.createOrder(request); }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ObjectNode> patchOrder(@PathVariable("orderId") Long orderId, @Valid @RequestBody OrderRequest request) { return orderService.updateOrder(orderId, request); }

    @GetMapping("/{orderId}")
    public ResponseEntity<ObjectNode> getOrder(@PathVariable("orderId") Long orderId){return orderService.getOrderById(orderId);}

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) { orderService.deleteOrder(orderId); }


}
