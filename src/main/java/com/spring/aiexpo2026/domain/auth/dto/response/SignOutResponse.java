package com.spring.aiexpo2026.domain.auth.dto.response;

public record SignOutResponse(
		String message
) {

	public static SignOutResponse of(String message) {
		return new SignOutResponse(message);
	}
}
