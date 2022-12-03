package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/products")
@RestController
@AllArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return productService.findAll();
    }

    @PostMapping
    public ResponseEntity<ObjectNode> createProducts(@Valid @RequestBody ProductRequest product) {
        return productService.create(product);
    }

}
