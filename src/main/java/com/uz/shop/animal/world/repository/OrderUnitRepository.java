package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.OrderUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * Repoozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
public interface OrderUnitRepository extends JpaRepository<OrderUnit, Long> {
   //Select odpowiadający za zbieranie orderu po ID
    @Query(
            value = "SELECT * from order_units ou Where ou.order_id = ?1",
            nativeQuery = true
    )
    Collection<OrderUnit> findByOrderId(Long order_id);
}
