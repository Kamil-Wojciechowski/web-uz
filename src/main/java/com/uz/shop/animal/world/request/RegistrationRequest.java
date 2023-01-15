package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

import static com.uz.shop.animal.world.utils.Dictionary.INVALID_INPUT;
import static com.uz.shop.animal.world.utils.Dictionary.WRONG_FORMAT_EMAIL;

/**
 * Requesty odpowiadają za zapytania, którę są wysyłane w stronę backendu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz konstruktor z wszystkimi elementami
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    //Element nie może być nullem oraz rozmiar musi mieć minimalnie 2
    @NotNull
    @Size(min=2, message = INVALID_INPUT)
    @JsonProperty("firstname")
    private final String firstname;
    @NotNull
    @Size(min=2, message = INVALID_INPUT)
    @JsonProperty("lastname")
    private final String lastname;
    @NotNull
    @Email(message = WRONG_FORMAT_EMAIL)
    @JsonProperty("email")
    private final String email;
    @NotNull
    @NotEmpty
    @JsonProperty("password")
    private final String password;
    @NotNull
    @NotEmpty
    @JsonProperty("confirmedPassword")
    private final String confirmedPassword;
//    @NotNull
//    @NotEmpty
    @JsonProperty("recaptchaToken")
    private final String recaptchaToken;
}
