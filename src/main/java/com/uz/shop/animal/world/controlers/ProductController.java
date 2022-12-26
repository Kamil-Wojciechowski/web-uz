package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.request.ProductPostRequest;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.services.ProductService;
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
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return productService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ObjectNode> createProduct(@Valid @RequestBody ProductPostRequest product) {
        return productService.create(product);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ObjectNode> patchProduct(@PathVariable("productId") Long productId,@Valid @RequestBody ProductRequest request)  {
        return productService.update(productId, request);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ObjectNode> getProduct(@PathVariable("productId") Long productId)  {
        return productService.getProductById(productId);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
    }
}
