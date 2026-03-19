package com.spring.aiexpo2026.domain.payment.service;

import com.spring.aiexpo2026.domain.payment.dto.request.PaymentConfirmRequest;
import com.spring.aiexpo2026.domain.payment.dto.response.PaymentConfirmResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;

public interface PaymentService {
	ApiResponse<PaymentConfirmResponse> confirmPayment(String username, PaymentConfirmRequest request);
}
