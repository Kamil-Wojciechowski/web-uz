package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.repository.ProductRepository;
import com.uz.shop.animal.world.repository.ProductTagRepository;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.uz.shop.animal.world.utils.Dictionary.ALREADY_EXISTS;
import static com.uz.shop.animal.world.utils.Dictionary.ITEM_NOT_FOUND;

@Service
public class ProductService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ObjectNode objectNode = new ObjectMapper().createObjectNode();
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    public ResponseEntity<List<Product>> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(productRepository.findAll());
        }

        return ResponseEntity.ok(new ArrayList<>(productRepository.findAllVisible()));
    }

    private ResponseEntity<ObjectNode> createRespones(Product product) {
        ObjectNode tree = mapper.valueToTree(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(tree);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ObjectNode> create(ProductRequest request) {
        if(productRepository.findByName(request.getName()).isPresent()) {
            return ErrorResponseCreator.buildBadRequest("Error", ALREADY_EXISTS);
        }

        if(!productTagRepository.findById(request.getProductTag()).isPresent()) {
            return ErrorResponseCreator.buildBadRequest("Error", ITEM_NOT_FOUND);
        }

        ProductTag productTag = productTagRepository.findById(request.getProductTag())
                .orElseThrow(() ->
                    new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
                );


        Product savedProduct = productRepository.save(new Product(
                productTag,
                request.getName(),
                request.getAmount(),
                request.getPriceUnit(),
                request.getIsVisible()
        ));

        return createRespones(savedProduct);

    }
}
