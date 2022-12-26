package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.request.OrderUnitRequest;
import com.uz.shop.animal.world.services.OrderUnitService;
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
@RequestMapping("api/v1/orders/{orderId}/units")
public class OrderUnitController {
    @Autowired
    private OrderUnitService orderUnitService;

    @GetMapping
    public ResponseEntity<List<OrderUnit>> getOrderUnits(@PathVariable("orderId") Long orderId) {return orderUnitService.getAll(orderId);}

    @PostMapping
    public ResponseEntity<ObjectNode> postOrder(@PathVariable("orderId") Long orderId, @Valid @RequestBody OrderUnitRequest request) { return orderUnitService.createOrderUnit(orderId, request); }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{orderUnitId}")
    public ResponseEntity<ObjectNode> patchOrder(@PathVariable("orderId") Long orderId, @PathVariable("orderUnitId") Long orderUnitId, @Valid @RequestBody OrderUnitRequest request) { return orderUnitService.updateOrderUnit(orderId, orderUnitId, request); }

    @GetMapping("/{orderUnitId}")
    public ResponseEntity<ObjectNode> getOrderUnit(@PathVariable("orderId") Long orderId, @PathVariable("orderUnitId") Long orderUnitId) { return orderUnitService.getByOrderUnitId(orderId, orderUnitId); }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{orderUnitId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId, @PathVariable("orderUnitId") Long orderUnitId) { orderUnitService.deleteOrderUnit(orderId, orderUnitId); }
}
