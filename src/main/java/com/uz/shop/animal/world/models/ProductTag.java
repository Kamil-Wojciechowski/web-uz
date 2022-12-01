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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Valid
    @NotEmpty
    @Size(min=2, message = INVALID_INPUT)
    @JsonProperty("name")
    private String name;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
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

