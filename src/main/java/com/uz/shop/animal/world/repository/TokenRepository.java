package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
@Repository
@Transactional(readOnly = true)
public interface TokenRepository extends JpaRepository<Token, Long> {
    //Token przez token
    Optional<Token> findByToken(String token);

    //Aktualizacja tokenu po tokenie oraz zaakcpetowania
    @Transactional
    @Modifying
    @Query(value = "Update tokens t SET t.confirmed_at = ?2 WHERE t.token = ?1", nativeQuery = true)
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
