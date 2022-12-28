package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(
            value = "SELECT * from orders o Where o.address_id IN (Select a.id from addresses a where a.user_id = ?1)",
            nativeQuery = true
    )
    Collection<Order> findByUserId(Long user_id);

    @Query(
            value = "SELECT * FROM orders WHERE id = ?1",
            nativeQuery = true
    )
    Order findOrderById(long id_order);
}
