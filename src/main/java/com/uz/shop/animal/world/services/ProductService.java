package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.models.ProductTag;
import com.uz.shop.animal.world.repository.ProductRepository;
import com.uz.shop.animal.world.repository.ProductTagRepository;
import com.uz.shop.animal.world.request.ProductPostRequest;
import com.uz.shop.animal.world.request.ProductRequest;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ResponseEntity<ObjectNode> create(ProductPostRequest request) {
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
                request.getDescription(),
                request.getAmount(),
                request.getPriceUnit(),
                request.getImageBase(),
                request.getVideoUrl(),
                request.getIsVisible()
        ));

        return respones(savedProduct, true);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
                );
    }

    private void setProduct(ProductRequest request, Product product) {
        Map<String, Object> map = request.getHashMap();

        map.forEach((key, value) -> {
            if(value != null) {
                switch (key) {
                    case "name":
                        product.setName(value.toString());
                        break;
                    case "productTag":
                        ProductTag productTag = productTagRepository.findById((Integer) value)
                                .orElseThrow(() ->
                                        new RestClientResponseException(TAG_NOT_FOUND, 400, HttpStatus.NOT_FOUND.name(), null, null, null)
                                );

                        product.setProductTag(productTag);
                        break;
                    case "description":
                        product.setDescription(value.toString());
                        break;
                    case "amount":
                        product.setAmount((Integer) value);
                        break;
                    case "priceUnity":
                        product.setPriceUnit((Double) value);
                        break;
                    case "imageBase":
                        product.setImageBase(value.toString());
                        break;
                    case "videoUrl":
                        product.setVideoUrl(value.toString());
                        break;
                    case "isVisible":
                        if(product.getIsVisible() != Boolean.valueOf(value.toString())) {
                            product.setIsVisible(Boolean.valueOf(value.toString()));
                        }
                        break;

                }
            }
        });
    }

    public ResponseEntity<ObjectNode> getProductById(Long productId) {
        Product product = getProduct(productId);

        return respones(product, false);
    }

    public ResponseEntity<ObjectNode> update(Long productId, ProductRequest request) {
        Product product = getProduct(productId);

        setProduct(request, product);

        Product savedProduct = productRepository.save(product);

        return respones(savedProduct, false);
    }

    public void delete(Long id) {
        Product product = getProduct(id);

        productRepository.delete(product);
    }
}
