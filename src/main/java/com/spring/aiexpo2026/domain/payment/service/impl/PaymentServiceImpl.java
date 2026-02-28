package com.spring.aiexpo2026.domain.payment.service.impl;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.payment.data.request.PaymentConfirmRequest;
import com.spring.aiexpo2026.domain.payment.data.response.PaymentConfirmResponse;
import com.spring.aiexpo2026.domain.payment.exception.PaymentStatusCode;
import com.spring.aiexpo2026.domain.payment.service.PaymentService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final MemberRepository memberRepository;

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Override
    @Transactional
    public ApiResponse<PaymentConfirmResponse> confirmPayment(String nickname, PaymentConfirmRequest request) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        callTossConfirmApi(request);

        memberRepository.chargeCredit(member.getId(), request.getAmount());

        return ApiResponse.ok(PaymentConfirmResponse.builder()
                .credit(member.getCredit() + request.getAmount())
                .build());
    }

    private void callTossConfirmApi(PaymentConfirmRequest request) {
        String credentials = tossSecretKey + ":";
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String requestBody = String.format(
                "{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%d}",
                request.getPaymentKey(), request.getOrderId(), request.getAmount()
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(TOSS_CONFIRM_URL))
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApplicationException(PaymentStatusCode.PAYMENT_CONFIRM_FAILED);
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(PaymentStatusCode.PAYMENT_CONFIRM_FAILED);
        }
    }
}
