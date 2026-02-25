package com.spring.aiexpo2026.domain.auth.dto.response;

public record SignInResponse(

		String accessToken
) {

	public static SignInResponse of(String accessToken) {
		return new SignInResponse(accessToken);
	}
}
