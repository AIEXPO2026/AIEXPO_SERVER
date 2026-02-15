package com.spring.aiexpo2026.domain.auth.dto.response;

public record SignUpResponse(
		String message
) {

	public static SignUpResponse of	(String message) {
		return new SignUpResponse(message);
	}
}
