package com.spring.aiexpo2026.domain.auth.dto.response;

public record VerifyEmailResponse(
		String message
) {

	public static VerifyEmailResponse success(String message) {
		return new VerifyEmailResponse(message);
	}
}
