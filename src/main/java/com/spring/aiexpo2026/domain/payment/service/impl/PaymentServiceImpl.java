package com.spring.aiexpo2026.domain.payment.service.impl;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.payment.dto.request.PaymentConfirmRequest;
import com.spring.aiexpo2026.domain.payment.dto.response.PaymentConfirmResponse;
import com.spring.aiexpo2026.domain.payment.service.PaymentService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final MemberRepository memberRepository;

	@Value("${toss.secret-key}")
	private String tossSecretKey;

	private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
	private static final int CREDIT_PER_WON = 1;

	@Override
	@Transactional
	public ApiResponse<PaymentConfirmResponse> confirmPayment(String username, PaymentConfirmRequest request) {
		Member member = memberRepository.findByNickname(username)
				.orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

		// Toss Payments 결제 승인 API 호출
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String encoded = Base64.getEncoder()
				.encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		headers.set("Authorization", "Basic " + encoded);

		Map<String, Object> body = Map.of(
				"paymentKey", request.paymentKey(),
				"orderId", request.orderId(),
				"amount", request.amount()
		);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<Map> tossResponse = restTemplate.exchange(
				TOSS_CONFIRM_URL, HttpMethod.POST, entity, Map.class
		);

		if (!tossResponse.getStatusCode().is2xxSuccessful()) {
			throw new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS);
		}

		// 결제 금액 → 크레딧 환산 후 충전
		int creditAmount = request.amount() * CREDIT_PER_WON;
		memberRepository.chargeCredit(member.getId(), creditAmount);

		int newCredit = member.getCredit() + creditAmount;
		return ApiResponse.ok(PaymentConfirmResponse.of(newCredit));
	}
}
