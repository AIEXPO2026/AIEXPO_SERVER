package com.spring.aiexpo2026.domain.payment.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private int amount;
}
