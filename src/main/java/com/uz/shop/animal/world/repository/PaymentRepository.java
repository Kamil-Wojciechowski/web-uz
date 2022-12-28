package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(
            value = "SELECT * FROM payments WHERE order_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true
    )
    Payment findLastPaymentByOrderId(long id_order);

    @Query(
            value = "SELECT * FROM payments WHERE order_id = ?1",
            nativeQuery = true
    )
    List<Payment> findPaymentsByOrderId(long id_order);

    @Query(
            value = "SELECT * FROM payments WHERE id = ?1",
            nativeQuery = true
    )
    Payment findPaymentById(long id);
}
