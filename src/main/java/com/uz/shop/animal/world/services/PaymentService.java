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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();


    private ResponseEntity<ObjectNode> response(Payment payment, Boolean isCreated) {
        ObjectNode tree = mapper.valueToTree(payment);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    public ResponseEntity<List<Payment>> getAllForOrder(long idOrder) {
        Order order = orderRepository.findOrderById(idOrder);

        if (!this.checkAccess(order)) {
            return new ResponseEntity<List<Payment>>(new ArrayList<Payment>(), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new ArrayList<Payment>(paymentRepository.findPaymentsByOrderId(idOrder)));
    }

    public ResponseEntity<Payment> getLastPaymentForOrder(long idOrder) {
        Order order = orderRepository.findOrderById(idOrder);

        if (!this.checkAccess(order)) {
            return new ResponseEntity<Payment>(new Payment(), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(paymentRepository.findLastPaymentByOrderId(idOrder));
    }

    public ResponseEntity<ObjectNode> createPayment(PaymentRequest request) {
        Order order = orderRepository.findOrderById(request.getOrderId());
        Payment payment = new Payment(order, request.getStatus(), request.getCallbackData());
        payment = paymentRepository.save(payment);

        return response(payment, true);
    }

    public ResponseEntity<ObjectNode> updatePayment(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findPaymentById(id);
        payment.setStatus(request.getStatus());
        payment.setCallback_data(request.getCallbackData());

        payment = paymentRepository.save(payment);

        return this.response(payment, false);
    }

    private boolean checkAccess(Order order) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long idUser = order.getAddress().getUser().getId();

        return UserType.ROLE_ADMIN == user.getUserType() || user.getId() == idUser;
    }
}
