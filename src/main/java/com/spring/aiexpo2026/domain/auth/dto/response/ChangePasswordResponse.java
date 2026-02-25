package com.spring.aiexpo2026.domain.auth.dto.response;


public record ChangePasswordResponse(
		String message
) {
	public static ChangePasswordResponse of(String message) {
		return new ChangePasswordResponse(message);
	}
}
