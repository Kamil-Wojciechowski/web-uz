package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Requesty odpowiadają za zapytania, którę są wysyłane w stronę backendu
 * Lombok pomaga nam utworzyć automatycznie gettery, settery, bezargumentowy konstruktor oraz konstruktor z wszystkimi elementami
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @JsonProperty("status")
    private String status;

    @JsonProperty("callback_data")
    private String callbackData;

    @JsonProperty("order_id")
    private Long orderId;
}
