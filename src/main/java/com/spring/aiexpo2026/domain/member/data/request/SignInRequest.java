package com.spring.aiexpo2026.domain.member.data.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

		@NotBlank
		String username,

		@NotBlank
		String password
) {
}
