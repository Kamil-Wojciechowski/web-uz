package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {

    @Size(min = 2, message = INVALID_INPUT)
    @JsonProperty("firstname")
    private String firstname;

    @Size(min = 2, message = INVALID_INPUT)
    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("company")
    private String company;

    @JsonProperty("nip")
    private String nip;

    @Size(min = 9, message = INVALID_INPUT)
    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @Size(min = 2, message = INVALID_INPUT)
    @JsonProperty("street")
    private String street;

    @Size(min = 6, message = INVALID_INPUT)
    @JsonProperty("postalCode")
    private String postalCode;

    @Size(min = 2, message = INVALID_INPUT)
    @JsonProperty("city")
    private String city;
}
