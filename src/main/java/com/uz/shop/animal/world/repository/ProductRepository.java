package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //Zbieranie produktu po ID
    @Override
    Optional<Product> findById(Long aLong);

    //Zbieranie produktu po nazwie
    Optional<Product> findByName(String name);

    //Zbieranie produktów, które są widoczne dla zwykłego użytkownika
    @Query(
            value = "SELECT * FROM products p WHERE p.is_visible = 1",
            nativeQuery = true)
    Collection<Product> findAllVisible();

    @Query(
            value = "SELECT * FROM products p WHERE p.is_visible = ?1 and p.product_tag_id = ?2",
            nativeQuery = true)
    Collection<Product> findByProductTagId(Boolean visible, Integer productTag);

    @Query(
            value = "SELECT * FROM products p WHERE p.is_visible = ?1 and p.product_tag_id = ?2",
            nativeQuery = true)
    Collection<Product> findByProductTagId(Integer productTag);
}
