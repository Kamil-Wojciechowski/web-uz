package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private static final Integer LENGTH_NAME_MIN = 2;

    @JsonProperty("productTag")
    private Integer productTag;

    @JsonProperty("name")
    private String name;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("priceUnit")
    private Double priceUnit;

    @JsonProperty("isVisible")
    private Boolean isVisible = false;

    public boolean isProductTagNull() {
        return this.productTag == null;
    }

    public boolean isNameNull() {
        return this.name == null;
    }

    public boolean isAmountNull() {
        return this.amount == null;
    }

    public boolean isPriceUnitNull() {
        return this.priceUnit == null;
    }

    public boolean isAnyFieldNull() {
        return isProductTagNull() || isNameNull() || isAmountNull() || isPriceUnitNull();
    }
    @AssertTrue
    public boolean isValidProductTag() {
        if(this.productTag == null) {
            return true;
        } else {
            return productTag > 0;
        }
    }
    @AssertTrue
    public boolean isValidAmount() {
        if(this.amount == null) {
            return true;
        } else {
            return this.amount > 0;
        }
    }

    @AssertTrue
    public boolean isValidPriceUnit() {
        if(this.priceUnit == null) {
            return true;
        } else {
            return this.priceUnit > 0;
        }
    }

    @AssertTrue
    public boolean isValidName() {
        if(this.name == null) {
            return true;
        } else {
            return this.name.length() > LENGTH_NAME_MIN;
        }
    }
}
