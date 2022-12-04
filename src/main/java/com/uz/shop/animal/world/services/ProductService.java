package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.repository.ProductRepository;
import com.uz.shop.animal.world.repository.ProductTagRepository;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.uz.shop.animal.world.utils.Dictionary.*;

@Service
@EnableAutoConfiguration
@AllArgsConstructor
public class ProductService {

    private final ObjectMapper mapper;
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

    private ResponseEntity<ObjectNode> respones(Product product, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(product);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    public ResponseEntity<ObjectNode> create(ProductRequest request) {
        if(request.isAnyFieldNull()) {
            return ErrorResponseCreator.buildBadRequest("Error", INVALID_INPUTS);
        }

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

        return respones(savedProduct, true);
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
                );
    }

    private void setProduct(ProductRequest request, Product product) {
        if(!request.isNameNull()) {
            if(productRepository.findByName(request.getName()).isPresent()) {
                throw new RestClientResponseException(ALREADY_EXISTS, 400, HttpStatus.BAD_REQUEST.name(), null, null, null);
            }
            product.setName(request.getName());
        }

        if(!request.isAmountNull()) {
            product.setAmount(request.getAmount());
        }

        if(!request.isProductTagNull()) {
            ProductTag productTag = productTagRepository.findById(request.getProductTag())
                    .orElseThrow(() ->
                            new RestClientResponseException(TAG_NOT_FOUND, 400, HttpStatus.NOT_FOUND.name(), null, null, null)
                    );

            product.setProductTag(productTag);
        }

        if(!request.isPriceUnitNull()) {
            product.setPriceUnit(request.getPriceUnit());
        }

        product.setIsVisible(request.getIsVisible());
    }

    public ResponseEntity<ObjectNode> update(Long productId, ProductRequest request) {
        Product product = getProductById(productId);

        setProduct(request, product);

        Product savedProduct = productRepository.save(product);

        return respones(savedProduct, false);
    }

    public ResponseEntity.BodyBuilder delete(Long id) {
        Product product = getProductById(id);

        productRepository.delete(product);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
