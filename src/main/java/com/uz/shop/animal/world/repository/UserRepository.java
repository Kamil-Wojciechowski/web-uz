package com.uz.shop.animal.world.repository;


import com.uz.shop.animal.world.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
   //Znalezienie użytkownika przez mail
    Optional<User> findByEmail(String email);

    //Aktualizacja użytkownika na aktywny przez mail
    @Transactional
    @Modifying
    @Query(value = "Update users u SET u.enabled = TRUE where u.email = ?1", nativeQuery = true)
    int enableUser(String email);

    //Zmiana hasła przez ID
    @Transactional
    @Modifying
    @Query(value = "Update users u SET u.password = ?1 where u.id = ?2", nativeQuery = true)
    int updatePassword(String password, Long id);
}
