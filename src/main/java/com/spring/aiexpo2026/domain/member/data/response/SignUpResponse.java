package com.spring.aiexpo2026.domain.member.data.response;

import org.springframework.http.HttpStatus;

public record SignUpResponse(

		HttpStatus code,

		String message
) {

	public static SignUpResponse success() {
		return new SignUpResponse(HttpStatus.OK, "회원가입 되었습니다.");
	}

	public static SignUpResponse of() {
		return new SignUpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 할 수 없습니다.");
	}
}
