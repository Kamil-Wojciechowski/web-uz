package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.Payment;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.repository.AddressRepository;
import com.uz.shop.animal.world.repository.OrderRepository;
import com.uz.shop.animal.world.repository.PaymentRepository;
import com.uz.shop.animal.world.request.OrderRequest;
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

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy
@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    private final PaymentRepository paymentRepository;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    /*
    Pobierane są wszystkie elemnenty dla admina, w innym przypadku są zwracane elementy tylko do należącego użytkownika.
     */
    public ResponseEntity<List<Order>> getAll(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(orderRepository.findAll());
        }

        return ResponseEntity.ok(new ArrayList<>(orderRepository.findByUserId(((User)auth.getPrincipal()).getId())));

    }

    //Przygotowane odpowiedzi z serwera
    private ResponseEntity<ObjectNode> responses(Order order, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(order);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    //Pobieranie zamówienia po ID
    private Order findOrderById(Long id) {
        return  orderRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null));
    }

    /*
    Tworzenie Zamówienia
    Pobierany jest adres oraz walidoawny.
    Zamówienie zostaje utworzone oraz zapisane w bazie.
     */
    public ResponseEntity<ObjectNode> createOrder(OrderRequest request) {

        Address address = addressRepository.findById(request.getAddress()).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null));

        Order order = new Order(address, request.getStatus());

        order = orderRepository.save(order);

        Payment payment = new Payment(order, "Initialized", "Initialized");

        paymentRepository.save(payment);

        return responses(order, true);
    }


    /*
    Pobieranie pojedynczego zamówienia po ID
     */
    public ResponseEntity<ObjectNode> getOrderById(Long id) {
        Order order = findOrderById(id);

        return responses(order, false);
    }

    /*
    Aktualizacja Zamówienia
    Wyszukiwane jest zamówienie.
    Ustawiany jest status oraz zapisywany.
    Zwracana jest odpowiedź.
     */
   public ResponseEntity<ObjectNode> updateOrder(Long id, OrderRequest request)
   {
       Order order = findOrderById(id);

       Authentication auth = SecurityContextHolder.getContext().getAuthentication();

       if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
           if(order.getOrderStatus().equals("Nowe")) {
               if(request.getStatus().equals("Opłacone") | request.getStatus().equals("Anulowane")) {
                   order.setOrderStatus(request.getStatus());
               }
           }
       } else {
           order.setOrderStatus(request.getStatus());
       }

        order = orderRepository.save(order);

        return responses(order, false);
    }

    /*
    Usuwanie Zamówienia,
    Wyszukiwane jest zamówienie a następnie usuwane.
     */
    public ResponseEntity.BodyBuilder deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null));
        ;

        orderRepository.delete(order);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
