package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.request.ProductTagRequest;
import com.uz.shop.animal.world.services.ProductTagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/products/tags")
@RestController
@AllArgsConstructor
public class ProductTagController {

    private final ProductTagService productTagService;

    @GetMapping
    public ResponseEntity<List<ProductTag>> productTags() {
        return productTagService.getAll();
    }

    @PostMapping
    public ResponseEntity<ObjectNode> createProductTag(@Valid @RequestBody ProductTagRequest productTag) {
        return productTagService.create(productTag);
    }

    @PatchMapping("/{productTagId}")
    public ResponseEntity<ObjectNode> updateProductTag(@PathVariable("productTagId") Integer productTagId, @Valid @RequestBody ProductTagRequest request) {
        return productTagService.update(productTagId, request);
    }

    @DeleteMapping("/{productTagId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductTag(@PathVariable("productTagId") Integer productTagId) {
        productTagService.delete(productTagId);
    }

}
