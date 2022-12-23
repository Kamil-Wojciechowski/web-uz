package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUnitRequest {
    @NotNull(message = INVALID_INPUT)
    @JsonProperty("productId")
    private Long productId;

    @NotNull(message = INVALID_INPUT)
    @JsonProperty("amount")
    private Integer amount;

}
