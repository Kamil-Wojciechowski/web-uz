package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTagRepository extends JpaRepository<ProductTag, Integer> {

    Optional<ProductTag> findById(Integer id);
    Optional<ProductTag> findByName(String name);


}
