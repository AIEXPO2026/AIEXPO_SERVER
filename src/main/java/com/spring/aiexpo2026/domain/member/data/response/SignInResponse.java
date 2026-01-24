package com.spring.aiexpo2026.domain.member.data.response;

public record SignInResponse(

		String accessToken
) {

	public static SignInResponse success(String accessToken) {
		return new SignInResponse(accessToken);
	}
}
