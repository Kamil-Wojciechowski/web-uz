package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.OrderUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OrderUnitRepository extends JpaRepository<OrderUnit, Long> {
    @Query(
            value = "SELECT * from order_units ou Where ou.order_id = ?1",
            nativeQuery = true
    )
    Collection<OrderUnit> findByOrderId(Long order_id);
}
