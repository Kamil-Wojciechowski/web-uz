package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.Payment;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.enums.UserType;
import com.uz.shop.animal.world.repository.OrderRepository;
import com.uz.shop.animal.world.repository.PaymentRepository;
import com.uz.shop.animal.world.request.PaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.uz.shop.animal.world.utils.Dictionary.ITEM_NOT_FOUND;

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy
@Service
@AllArgsConstructor
public class PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();


    //Odpowiedzi serwera Create oraz Update
    private ResponseEntity<ObjectNode> response(Payment payment, Boolean isCreated) {
        ObjectNode tree = mapper.valueToTree(payment);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    /*
    Listowanie wszystkich płatności dla zamówień
    Pobierane zostają wszystkie płatności poprzez ID zamówienia.
    Następnie zależnie od uprawnień zwracany jest odpowiedź.
     */
    public ResponseEntity<List<Payment>> getAllForOrder(long idOrder) {
        Order order = orderRepository.findById(idOrder).orElseThrow(() -> {
            throw new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null);
        });

        if (!this.checkAccess(order)) {
            return new ResponseEntity<List<Payment>>(new ArrayList<Payment>(), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new ArrayList<Payment>(paymentRepository.findPaymentsByOrderId(idOrder)));
    }

    //Pobieranie ostatniej płatności za zamówienie - Logika jak powyżej
    public ResponseEntity<Payment> getLastPaymentForOrder(long idOrder) {
        Order order = orderRepository.findById(idOrder).orElseThrow(() -> {
            throw new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null);
        });

        if (!this.checkAccess(order)) {
            return new ResponseEntity<Payment>(new Payment(), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(paymentRepository.findLastPaymentByOrderId(idOrder).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null)
        ));
    }

    /*
    Tworzenie Płatności
    Pobierany jest order po Id,
    Tworzony jest payment z wartościami z body.
    Płatność jest zapisana w bazie.
     */
    public ResponseEntity<ObjectNode> createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> {
            throw new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null);
        });

        Payment payment = new Payment(order, request.getStatus(), request.getCallbackData());
        payment = paymentRepository.save(payment);

        return response(payment, true);
    }

    /*
    Aktualizacja Płatności
    Pobierana jest płatność.
    Ustawiany jest status oraz callback data.
    Płatnośc jest zapisywana.
     */
    public ResponseEntity<ObjectNode> updatePayment(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findPaymentById(id).orElseThrow(()->
                new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null)
        );;
        payment.setStatus(request.getStatus());
        payment.setCallback_data(request.getCallbackData());

        payment = paymentRepository.save(payment);

        return this.response(payment, false);
    }

    //Sprawdzenie dostępu użytkownika do elementu.
    private boolean checkAccess(Order order) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long idUser = order.getAddress().getUser().getId();

        return UserType.ROLE_ADMIN == user.getUserType() || user.getId() == idUser;
    }
}
