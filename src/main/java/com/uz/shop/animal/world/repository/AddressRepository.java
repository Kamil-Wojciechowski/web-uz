package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    //Select odpowiadający za zbieranie wszystkich adresów, które należą do użytkownika
    @Query(
            value = "SELECT * from addresses a Where a.user_id IN (Select u.id from users u where u.email = ?1 )",
            nativeQuery = true
    )
    Collection<Address> findByUserEmail(String email);
}
