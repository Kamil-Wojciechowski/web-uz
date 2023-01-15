package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Requesty odpowiadają za zapytania, którę są wysyłane w stronę backendu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz konstruktor z wszystkimi elementami
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RecoveryRequest {
    //Sprawdza hasło czy jego size jest minimalnie na 8 znaków
    @Valid
    @Size(min = 8)
    @JsonProperty("password")
    private String password;

    @Valid
    @Size(min = 8)
    @JsonProperty("confirmedPassword")
    private String confirmedPassword;
}
