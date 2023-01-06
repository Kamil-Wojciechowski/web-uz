package com.uz.shop.animal.world.repository;

import com.uz.shop.animal.world.models.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repozytoria pozwalają nam na połączenie się do bazy, utworzenie encji oraz zarządzanie nimi
 */
public interface ProductTagRepository extends JpaRepository<ProductTag, Integer> {

    //Tag produktu przez ID
    Optional<ProductTag> findById(Integer id);

    //Tag produktu przez nazwe
    Optional<ProductTag> findByName(String name);


}
