package com.spring.aiexpo2026.domain.member.data.response;

import org.springframework.http.HttpStatus;

public record ChangePasswordResponse(
		HttpStatus code,

		String message
) {

	public static ChangePasswordResponse success() {
		return new ChangePasswordResponse(HttpStatus.OK, "변경되었습니다.");
	}
}
