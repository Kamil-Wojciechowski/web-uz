package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    @NotNull
    @JsonProperty("firstname")
    private final String firstname;
    @NotNull
    @JsonProperty("lastname")
    private final String lastname;
    @NotNull
    @JsonProperty("email")
    private final String email;
    @NotNull
    @JsonProperty("password")
    private final String password;
    @NotNull
    @JsonProperty("confirmedPassword")
    private final String confirmedPassword;
    @NotNull
    @JsonProperty("recaptchaToken")
    private final String recaptchaToken;
}
