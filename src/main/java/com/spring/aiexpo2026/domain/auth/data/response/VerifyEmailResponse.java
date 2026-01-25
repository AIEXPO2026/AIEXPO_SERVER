package com.spring.aiexpo2026.domain.auth.data.response;

import org.springframework.http.HttpStatus;

public record VerifyEmailResponse(
		HttpStatus status,
		String message
) {

	public static VerifyEmailResponse success() {
		return new VerifyEmailResponse(HttpStatus.OK, "이메일이 인증되었습니다.");
	}
}
