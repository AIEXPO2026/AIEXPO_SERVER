package com.spring.aiexpo2026.domain.auth.dto.response;

import org.springframework.http.HttpStatus;

public record SignUpResponse(
		String message
) {

	public static SignUpResponse of	(String message) {
		return new SignUpResponse(message);
	}
}
