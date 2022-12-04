package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.repository.ProductTagRepository;
import com.uz.shop.animal.world.request.ProductTagRequest;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

import static com.uz.shop.animal.world.utils.Dictionary.*;

@Service
@AllArgsConstructor
public class ProductTagService {
    @Autowired
    private final ProductTagRepository productTagRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResponseEntity<List<ProductTag>> getAll() {
        return ResponseEntity.ok(productTagRepository.findAll());
    }

    private ProductTag getProductTag(Integer id) {
        return productTagRepository.findById(id).orElseThrow(()-> new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null));
    }
    private ResponseEntity<ObjectNode> createResponse(ProductTag productTag) {
        ObjectNode tree = mapper.valueToTree(productTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tree);
    }

    public ResponseEntity<ObjectNode> create(ProductTagRequest request) {
        boolean productExists = productTagRepository.findByName(request.getName()).isPresent();

        if(productExists) {
            return ErrorResponseCreator.buildBadRequest("Error", ALREADY_EXISTS);
        }

        Integer item = request.getParent();

        ProductTag productParent;
        ProductTag productTag;

        if(item != 0) {
             productParent = productTagRepository.findById(request.getParent()).orElseThrow(() -> new RestClientResponseException(PRODUCT_PARENT_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null));

             productTag = new ProductTag(request.getName(), productParent);
        } else {
            productTag = new ProductTag(request.getName());
        }

        productTag = productTagRepository.save(productTag);

        return createResponse(productTag);
    }

    private ResponseEntity<ObjectNode> updateResponse(ProductTag productTag) {
        ObjectNode tree = mapper.valueToTree(productTag);
        return ResponseEntity.ok(tree);
    }

    public ResponseEntity<ObjectNode> update(Integer id, ProductTagRequest request) {
        ProductTag productTag = getProductTag(id);

        if(request.getParent() == null) {
            productTag.setParent(null);
        } else if (request.getParent() != 0) {
            ProductTag parent = productTagRepository.findById(request.getParent())
                    .orElseThrow(() ->
                            new RestClientResponseException(PRODUCT_PARENT_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
                    );

            productTag.setParent(parent);
        }

        if(!request.getName().isBlank()) {
            productTag.setName(request.getName());
        }

        ProductTag updated = productTagRepository.save(productTag);

        return updateResponse(updated);
    }

    public ResponseEntity.BodyBuilder delete(Integer id) {
        ProductTag product = getProductTag(id);

        productTagRepository.delete(product);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
