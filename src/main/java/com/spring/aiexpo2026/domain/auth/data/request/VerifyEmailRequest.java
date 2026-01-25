package com.spring.aiexpo2026.domain.auth.data.request;

public record VerifyEmailRequest(
		String email,
		String authNum
) {
}
