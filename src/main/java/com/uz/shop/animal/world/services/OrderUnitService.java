package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.*;
import com.uz.shop.animal.world.repository.OrderRepository;
import com.uz.shop.animal.world.repository.OrderUnitRepository;
import com.uz.shop.animal.world.repository.ProductRepository;
import com.uz.shop.animal.world.request.OrderRequest;
import com.uz.shop.animal.world.request.OrderUnitRequest;
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

import static com.uz.shop.animal.world.utils.Dictionary.ITEM_NOT_FOUND;

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

    private Order findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Order", null, null, null)
        );
    }

    private OrderUnit findOrderUnit(Long id) {
        return orderUnitRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Order Unit", null, null, null)
        );
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name() + " Product", null, null, null)
        );
    }

    public ResponseEntity<List<OrderUnit>> getAll(Long orderId){
        Order order = findOrder(orderId);

        return ResponseEntity.ok(new ArrayList<>(orderUnitRepository.findByOrderId(order.getId())));
    }

    private ResponseEntity<ObjectNode> responses(OrderUnit orderUnit, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(orderUnit);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }
    public ResponseEntity<ObjectNode> createOrderUnit(Long orderId, OrderUnitRequest request) {
        Order order = findOrder(orderId);

        Product product = findProduct(request.getProductId());

        OrderUnit orderUnit = new OrderUnit(product, order, request.getAmount());

        orderUnit = orderUnitRepository.save(orderUnit);

        return responses(orderUnit, true);
    }

    public ResponseEntity<ObjectNode> updateOrderUnit(Long orderId, Long orderUnitId, OrderUnitRequest request) {
        Order order = findOrder(orderId);

        OrderUnit orderUnit = findOrderUnit(orderUnitId);

        Product product = findProduct(request.getProductId());

        orderUnit.setOrder(order);
        orderUnit.setProduct(product);
        orderUnit.setAmount(request.getAmount());

        orderUnit = orderUnitRepository.save(orderUnit);

        return responses(orderUnit, false);
    }

    public ResponseEntity.BodyBuilder deleteOrderUnit(Long orderId, Long UnitOrderId) {
        findOrder(orderId);

        OrderUnit orderUnit = findOrderUnit(UnitOrderId);

        orderUnitRepository.delete(orderUnit);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
