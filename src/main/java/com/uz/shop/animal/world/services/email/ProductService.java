package com.uz.shop.animal.world.services.email;

import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public ResponseEntity<List<Product>> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(productRepository.findAll());
        }

        return ResponseEntity.ok(new ArrayList<>(productRepository.findAllVisible()));
    }
}
