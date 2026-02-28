package com.spring.aiexpo2026.domain.payment.controller;

import com.spring.aiexpo2026.domain.payment.data.request.PaymentConfirmRequest;
import com.spring.aiexpo2026.domain.payment.data.response.PaymentConfirmResponse;
import com.spring.aiexpo2026.domain.payment.service.PaymentService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ApiResponse<PaymentConfirmResponse> confirm(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PaymentConfirmRequest request
    ) {
        return paymentService.confirmPayment(userDetails.getUsername(), request);
    }
}
