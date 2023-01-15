package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;
/**
 * Requesty odpowiadają za zapytania, którę są wysyłane w stronę backendu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz konstruktor z wszystkimi elementami
 */
@Data
@Valid
@Getter
public class ProductTagRequest {
    private static final Integer LENGTH_NAME_MIN = 2;
    @JsonProperty("name")
    private String name = "";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("parentId")
    private Integer parent = 0;

    @AssertTrue
    public boolean isValidValue() {
        if(this.name == null) {
            return true;
        } else {
            return this.name.length() > LENGTH_NAME_MIN;
        }
    }
}
