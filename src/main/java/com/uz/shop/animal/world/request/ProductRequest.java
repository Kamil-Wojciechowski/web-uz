package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotNull
    @JsonProperty("productTag")
    private Integer productTag;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("amount")
    private Integer amount;

    @NotNull
    @JsonProperty("priceUnit")
    private Double priceUnit;

    @NotNull
    @JsonProperty("isVisible")
    private Boolean isVisible = false;
}
