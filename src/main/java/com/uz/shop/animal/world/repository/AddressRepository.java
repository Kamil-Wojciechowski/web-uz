package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(
            value = "SELECT * from address a Where a.user_id IN (Select u.id from user u where u.email = ?1 )",
            nativeQuery = true
    )
    Collection<Address> findByUserEmail(String email);
}
