package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.*;
import com.uz.shop.animal.world.repository.OrderRepository;
import com.uz.shop.animal.world.repository.OrderUnitRepository;
import com.uz.shop.animal.world.repository.ProductRepository;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.request.OrderUnitRequest;
import com.uz.shop.animal.world.utils.ErrorResponseCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.uz.shop.animal.world.utils.Dictionary.*;
import static com.uz.shop.animal.world.utils.ErrorResponseCreator.buildBadRequest;

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy
@Service
@AllArgsConstructor
public class OrderUnitService {
    @Autowired
    private final OrderUnitRepository orderUnitRepository;
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ProductRepository productRepository;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    //Pobieranie zamówienia po ID
    private Order findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Order", null, null, null)
        );
    }

    //Pobieranie lini zamówienia po ID
    private OrderUnit findOrderUnit(Long id) {
        return orderUnitRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Order Unit", null, null, null)
        );
    }

    //Pobieranie produktu po ID
    private Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Product", null, null, null)
        );
    }

    //Pobieranie listy lini zamówiwień
    public ResponseEntity<List<OrderUnit>> getAll(Long orderId){
        Order order = findOrder(orderId);

        return ResponseEntity.ok(new ArrayList<>(orderUnitRepository.findByOrderId(order.getId())));
    }

    //Przygotowanie odpowiedzi z serwera na Create oraz Update
    private ResponseEntity<ObjectNode> responses(OrderUnit orderUnit, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(orderUnit);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }


    // Walidacja możliwych produktów do kupienia
    private void validateAmountBought(Integer productAmount, Integer bought) {
        if(productAmount < bought) {
            throw new RestClientResponseException(ITEM_COUNT_NOT_ENOUGH, 400, HttpStatus.BAD_REQUEST.name() + " Product", null, null, null);
        }
    }
    /*
    Tworzenie Lini Zamówienia
    Sprawdzany jest produkt czy jest widoczny oraz czy ilość do zakupienia jest możlwia.
    Pobierany jest order, produkt, tworzony jest orderUnit, a następnie zapisywany.
     */
    public ResponseEntity<ObjectNode> createOrderUnit(Long orderId, OrderUnitRequest request) {
        Order order = findOrder(orderId);

        Product product = findProduct(request.getProductId());

        if(!product.getIsVisible()) {
            return buildBadRequest(ITEM_NOT_AVALIABLE, "Error");
        }

        validateAmountBought((product.getAmount() - product.getAmountBought()), request.getAmount());

        OrderUnit orderUnit = new OrderUnit(product, order, request.getAmount());

        orderUnit = orderUnitRepository.save(orderUnit);

        product.setAmountBought(product.getAmountBought() + request.getAmount());

        productRepository.save(product);

        return responses(orderUnit, true);
    }

    /*
    Pobieranie Lini zamówień po ID
     */
    public ResponseEntity<ObjectNode> getByOrderUnitId(Long orderId, Long orderUnitId) {
        findOrder(orderId);

        OrderUnit orderUnit = findOrderUnit(orderUnitId);

        return responses(orderUnit, false);
    }


    /*
    Aktualizacja Lini zamówień
    Wyszukiwane jest zamówienie, linia zamówienia, produkt.
    Produkt jest sprawdzany, czy jest widoczny dla kupujących oraz, czy jego ilość jest wystarczająca do zakupu.
    Następnie odpowiednio ustawiane w lini zamówienia oraz zapisywany.
     */
    public ResponseEntity<ObjectNode> updateOrderUnit(Long orderId, Long orderUnitId, OrderUnitRequest request) {
        Order order = findOrder(orderId);

        OrderUnit orderUnit = findOrderUnit(orderUnitId);

        Product product = findProduct(request.getProductId());

        if(!product.getIsVisible()) {
            return buildBadRequest(ITEM_NOT_AVALIABLE, "Error");
        }

        Integer previousOrderAmount = orderUnit.getAmount();
        Integer alreadyBoughtExceptCurrentOrder = (product.getAmountBought() - previousOrderAmount);

        validateAmountBought((product.getAmount() - alreadyBoughtExceptCurrentOrder), request.getAmount());

        orderUnit.setOrder(order);
        orderUnit.setProduct(product);
        orderUnit.setAmount(request.getAmount());

        product.setAmountBought( alreadyBoughtExceptCurrentOrder + request.getAmount());

        productRepository.save(product);

        orderUnit = orderUnitRepository.save(orderUnit);

        return responses(orderUnit, false);
    }

    /*
    Usuwanie Lini zamówień
    Zamówienie oraz linia zamówienia sprawdzana jest, czy istnieje.
    Linia zamówienia zostaje usunięta.
     */
    public ResponseEntity.BodyBuilder deleteOrderUnit(Long orderId, Long UnitOrderId) {
        findOrder(orderId);

        OrderUnit orderUnit = findOrderUnit(UnitOrderId);

        Product product = findProduct(orderUnit.getId());
        product.setAmountBought(product.getAmountBought() - orderUnit.getAmount());
        productRepository.save(product);

        orderUnitRepository.delete(orderUnit);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
