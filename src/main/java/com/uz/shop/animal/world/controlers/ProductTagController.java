package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.request.ProductTagRequest;
import com.uz.shop.animal.world.services.ProductTagService;
import lombok.AllArgsConstructor;
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
@RequestMapping("api/v1/products/tags")
public class ProductTagController {

    private final ProductTagService productTagService;

    @GetMapping
    public ResponseEntity<List<ProductTag>> productTags() {
        return productTagService.getAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ObjectNode> createProductTag(@Valid @RequestBody ProductTagRequest productTag) {
        return productTagService.create(productTag);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{productTagId}")
    public ResponseEntity<ObjectNode> updateProductTag(@PathVariable("productTagId") Integer productTagId, @Valid @RequestBody ProductTagRequest request) {
        return productTagService.update(productTagId, request);
    }

    @GetMapping("/{productTagId}")
    public ResponseEntity<ObjectNode> getProductTagById(@PathVariable("productTagId") Integer productTagId) {
        return productTagService.getProductTagById(productTagId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{productTagId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductTag(@PathVariable("productTagId") Integer productTagId) {
        productTagService.delete(productTagId);
    }

}
