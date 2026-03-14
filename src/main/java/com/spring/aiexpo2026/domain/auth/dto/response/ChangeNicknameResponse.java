package com.spring.aiexpo2026.domain.auth.dto.response;

public record ChangeNicknameResponse(
		String message
) {

	public static ChangeNicknameResponse of(String message) {
		return new ChangeNicknameResponse(message);
	}
}
