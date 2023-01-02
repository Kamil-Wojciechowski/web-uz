package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

/**
 * Model Produktu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery oraz bezargumentowy konstruktor
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name="products")
public class Product {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_tag_id",  referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("productTag")
    private ProductTag productTag;

    //Tagi oznaczają, że kolumna nie może być pusta w bazie danych oraz odpowiednik klucza w REST jest "name"
    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("description")
    private String description;

    @Column(nullable = false)
    @JsonProperty("amount")
    private Integer amount;

    @Column(nullable = false)
    @JsonProperty("amountBought")
    private Integer amountBought;

    @Transient
    @JsonProperty("available")
    private Integer available;

    @Column(nullable = false)
    @JsonProperty("priceUnit")
    private Double priceUnit;

    @Column(nullable = false)
    @JsonProperty("imageBase")
    private String imageBase;

    @Column
    @JsonProperty("videoUrl")
    private String videoUrl;

    @JsonProperty("isVisible")
    private Boolean isVisible = false;

    //Flagi odpowiadają za automatyczne tworzenie czasu utworzenia oraz aktualizacji
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product(ProductTag productTag, String name, String description, Integer amount, Integer amountBought, Double priceUnit, String imageBase, String videoUrl, Boolean isVisible) {
        this.productTag = productTag;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.priceUnit = priceUnit;
        this.imageBase = imageBase;
        this.videoUrl = videoUrl;
        this.amountBought = amountBought;
        this.isVisible = isVisible;
    }
}
