package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPostRequest {

    private static final Integer LENGTH_NAME_MIN = 2;

    @Min(value = 1, message = INVALID_INPUT)
    @JsonProperty("productTag")
    private Integer productTag;

    @NotEmpty(message = INVALID_INPUT)
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = INVALID_INPUT)
    @JsonProperty("description")
    private String description;

    @Min(value = 1, message = INVALID_INPUT)
    @JsonProperty("amount")
    private Integer amount;

    @DecimalMin(value = "0.01", message = INVALID_INPUT)
    @JsonProperty("priceUnit")
    private Double priceUnit;

    @JsonProperty("isVisible")
    private Boolean isVisible = false;

    @NotEmpty(message = INVALID_INPUT)
    @JsonProperty("imageBase")
    private String imageBase;

    @JsonProperty("videoUrl")
    private String videoUrl;
}
