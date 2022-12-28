package com.uz.shop.animal.world.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
