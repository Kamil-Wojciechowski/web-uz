package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private static final Integer LENGTH_NAME_MIN = 2;

    @JsonProperty("productTag")
    private Integer productTag = 0;

    @JsonProperty("name")
    private String name = "0";

    @JsonProperty("description")
    private String description = "0";
    @JsonProperty("amount")
    private Integer amount = 0;

    @JsonProperty("priceUnit")
    private Double priceUnit = 0.0;

    @JsonProperty("isVisible")
    private Boolean isVisible = false;
    @JsonProperty("imageBase")
    private String imageBase = "0";

    @JsonProperty("videoUrl")
    private String videoUrl = "0";
    private void putEntry(Map<String, Object> map, String name, Object item) {
        map.put(name, item);
    }
    public Map<String, Object> getHashMap() {
        Map<String, Object> map = new HashMap<>();

        putEntry(map, "name", name);
        putEntry(map,"description", description);
        putEntry(map,"productTag", productTag);
        putEntry(map,"amount", amount);
        putEntry(map,"priceUnit", priceUnit);
        putEntry(map,"isVisible", isVisible);
        putEntry(map,"imageBase", imageBase);
        putEntry(map,"videoUrl", videoUrl);

        return map;
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

    @AssertTrue
    public boolean isValidDescription() {
        if(this.description == null) {
            return true;
        } else {
            return this.description.length() > LENGTH_NAME_MIN;
        }
    }

    @AssertTrue
    public boolean isValidVideoUrl() {
        if(this.videoUrl == null) {
            return true;
        } else {
            return this.videoUrl.length() > 0;
        }
    }
}
