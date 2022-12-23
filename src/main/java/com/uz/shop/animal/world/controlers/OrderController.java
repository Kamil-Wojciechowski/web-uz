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

@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrder(){return orderService.getAll();}

    @PostMapping
    public ResponseEntity<ObjectNode> postOrder(@Valid @RequestBody OrderRequest request) { return orderService.createOrder(request); }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{orderId}")
    public ResponseEntity<ObjectNode> patchOrder(@PathVariable("orderId") Long orderId, @Valid @RequestBody OrderRequest request) { return orderService.updateOrder(orderId, request); }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) { orderService.deleteOrder(orderId); }


}
