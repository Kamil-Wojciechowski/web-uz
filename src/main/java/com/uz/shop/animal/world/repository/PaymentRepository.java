package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Repoozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Zbieranie płatności po id
    @Query(
            value = "SELECT * FROM payments WHERE order_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true
    )
    Payment findLastPaymentByOrderId(long id_order);

    //Zbieranie płatności po zamówieniu
    @Query(
            value = "SELECT * FROM payments WHERE order_id = ?1",
            nativeQuery = true
    )
    List<Payment> findPaymentsByOrderId(long id_order);

    //Zbieranie płatności po ID
    @Query(
            value = "SELECT * FROM payments WHERE id = ?1",
            nativeQuery = true
    )
    Payment findPaymentById(long id);
}
