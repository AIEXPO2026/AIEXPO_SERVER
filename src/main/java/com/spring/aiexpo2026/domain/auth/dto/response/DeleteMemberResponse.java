package com.spring.aiexpo2026.domain.auth.dto.response;

public record DeleteMemberResponse(
		String message
) {

	public static DeleteMemberResponse of(String message) {
		return new DeleteMemberResponse(message);
	}
}
