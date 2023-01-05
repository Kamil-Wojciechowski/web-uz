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
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.uz.shop.animal.world.utils.Dictionary.*;

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy

@Service
@EnableAutoConfiguration
@AllArgsConstructor
public class ProductService {

    private final ObjectMapper mapper;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTagRepository;


    //Enkoduje byte to String w base64 dla frontendu oraz zapisuje pod wartoscia imageBase
    private void setProductImageBase(Product product)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.getEncoder().encode(product.getImage())));

        product.setImageBase(sb.toString());
    }

    //Przelicza dostępną ilość
    private void setProductAvailable(Product product)
    {
        product.setAvailable(product.getAmount() - product.getAmountBought());
    }

    /*
    Pobieranie produktów.
    Jeśli użytkownik zalogowany jest admin pobierane są wszystkie elementy.
    W innym przypadku zwracane są tylko elementy ustawione jako widoczne.
     */
    public ResponseEntity<List<Product>> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Product> products;

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            products = productRepository.findAll();
        } else {
            products = new ArrayList<>(productRepository.findAllVisible());
        }

        for(Product product : products) {
            setProductImageBase(product);
            setProductAvailable(product);
        }

        return ResponseEntity.ok(products);
    }

    // Odpowiedzi Created oraz Update
    private ResponseEntity<ObjectNode> respones(Product product, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(product);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    /*
    Tworzenie produktu
    Produkt jest walidowany, jeśli chodzi o nazwę oraz produkt tag.
    Zapisywany jest w bazie, a następnie zwracana jest odpowiedź z serwera.
     */
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

        byte[] decodedImage = Base64.getDecoder().decode(request.getImageBase());

        Product savedProduct = productRepository.save(new Product(
                productTag,
                request.getName(),
                request.getDescription(),
                request.getAmount(),
                0,
                request.getPriceUnit(),
                decodedImage,
                request.getVideoUrl(),
                request.getIsVisible()
        ));

        return respones(savedProduct, true);
    }

    //Pobieranie produktu po ID
    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
                );
    }

    /*
    Metoda odpowiadająca za aktualizacje produktu.
    Pierw pobierana jest HashMapa z requesta, a następnie mapowana względem odpowiednich kluczy do swoich wartości.
    Dalej tam walidoawna czy powinna zostać umieszczona.
     */
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
                        byte[] decodedImage = Base64.getDecoder().decode(value.toString());

                        product.setImage(decodedImage);
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

    //Pobieranie produktu po ID
    public ResponseEntity<ObjectNode> getProductById(Long productId) {
        Product product = getProduct(productId);

        setProductImageBase(product);
        setProductAvailable(product);

        return respones(product, false);
    }

    /* Aktualizacja produktu
    Używana jest tutaj metoda wcześniej opisana do ustawiania produktu.
    Produkt jest pobierany, następnie aktualizowany i zapisywany.
    Odpowiedź z serwera zostaje przygotowana i zwracana.
     */
    public ResponseEntity<ObjectNode> update(Long productId, ProductRequest request) {
        Product product = getProduct(productId);

        setProduct(request, product);

        Product savedProduct = productRepository.save(product);

        return respones(savedProduct, false);
    }

    //Usuwanie produkut, pobiera produkt po ID a następnie go usuwa
    public void delete(Long id) {
        Product product = getProduct(id);

        productRepository.delete(product);
    }
}
