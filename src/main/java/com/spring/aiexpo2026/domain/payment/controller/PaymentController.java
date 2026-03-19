package com.spring.aiexpo2026.domain.payment.controller;

import com.spring.aiexpo2026.domain.payment.dto.request.PaymentConfirmRequest;
import com.spring.aiexpo2026.domain.payment.dto.response.PaymentConfirmResponse;
import com.spring.aiexpo2026.domain.payment.service.PaymentService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping("/confirm")
	public ApiResponse<PaymentConfirmResponse> confirmPayment(
			@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody PaymentConfirmRequest request
	) {
		return paymentService.confirmPayment(userDetails.getUsername(), request);
	}
}
