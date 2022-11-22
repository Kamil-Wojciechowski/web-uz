package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RecoveryRequest {
    @Valid
    @Size(min = 8)
    @JsonProperty("password")
    private String password;

    @Valid
    @Size(min = 8)
    @JsonProperty("confirmedPassword")
    private String confirmedPassword;
}
