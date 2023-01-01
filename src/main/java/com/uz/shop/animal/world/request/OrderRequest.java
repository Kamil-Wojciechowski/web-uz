package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;

/**
 * Requesty odpowiadają za zapytania, którę są wysyłane w stronę backendu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz konstruktor z wszystkimi elementami
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    //NotNull oznacza, że element nie może być nullem
    @NotNull(message = INVALID_INPUT)
    @JsonProperty("address")
    private Long address;

    @JsonProperty("status")
    private String status;
}
