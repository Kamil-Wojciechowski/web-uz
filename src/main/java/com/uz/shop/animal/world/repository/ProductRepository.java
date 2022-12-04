package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long aLong);

    Optional<Product> findByName(String name);

    @Query(
            value = "SELECT * FROM product p WHERE p.is_visible = 1",
            nativeQuery = true)
    Collection<Product> findAllVisible();
}
