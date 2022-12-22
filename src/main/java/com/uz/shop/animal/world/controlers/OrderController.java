package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
