package com.spring.aiexpo2026.domain.payment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PaymentConfirmRequest(
		@NotBlank String paymentKey,
		@NotBlank String orderId,
		@Min(1) int amount
) {
}
