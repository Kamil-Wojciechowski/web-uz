package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public ResponseEntity<List<Order>> getAll(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(orderRepository.findAll());
        }

        return ResponseEntity.ok(new ArrayList<>(orderRepository.findByUserId(((User)auth.getPrincipal()).getId())));

    }
}
