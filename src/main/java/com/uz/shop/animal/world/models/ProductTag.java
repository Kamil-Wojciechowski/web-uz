package com.uz.shop.animal.world.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;
/**
 * Model Tagów produktów
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz
 * Tag Entity powoduje utworzenie elementu w bazie danych
 */
@Getter
@Setter
@Entity(name="product_tags")
@NoArgsConstructor
@AllArgsConstructor
public class ProductTag {
    // Tag ID oraz GeneratedValue oznacza, że kolumna jest Primary Key oraz wartość jest generowana
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    //Tagi oznaczają, że wartość musi być zatwierdzona, wypełniona, z minimalną długością 2
    @Valid
    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    @JsonProperty("name")
    private String name;

    //Tagi poniżej pozwalają utworzyć relacje w bazie danych
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonProperty("parentId")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductTag parent;

    public ProductTag(String name, ProductTag parent) {
        this.name = name;
        this.parent = parent;
    }

    public ProductTag(String name) {
        this.name = name;
    }
}

