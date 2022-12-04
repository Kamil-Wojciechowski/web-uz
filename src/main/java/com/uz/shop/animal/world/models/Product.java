package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_tag_id",  referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty("productTag")
    private ProductTag productTag;

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

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product(ProductTag productTag, String name, String description, Integer amount, Double priceUnit, String imageBase, String videoUrl, Boolean isVisible) {
        this.productTag = productTag;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.priceUnit = priceUnit;
        this.imageBase = imageBase;
        this.videoUrl = videoUrl;
        this.isVisible = isVisible;
    }
}
