package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
public interface OrderRepository extends JpaRepository<Order,Long> {

    //Select odpowiadający za znalezienie wszystkich zamówień użytkownika
    @Query(
            value = "SELECT * from orders o Where o.address_id IN (Select a.id from addresses a where a.user_id = ?1)",
            nativeQuery = true
    )
    Collection<Order> findByUserId(Long user_id);

    //Select odpowiadający za znalezienie zamówienia po ID
    @Query(
            value = "SELECT * FROM orders WHERE id = ?1",
            nativeQuery = true
    )
    Optional<Order> findOrderById(long id_order);
}
