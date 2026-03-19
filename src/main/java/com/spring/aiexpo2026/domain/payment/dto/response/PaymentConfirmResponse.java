package com.spring.aiexpo2026.domain.payment.dto.response;

public record PaymentConfirmResponse(
		int credit
) {
	public static PaymentConfirmResponse of(int credit) {
		return new PaymentConfirmResponse(credit);
	}
}
