package com.spring.aiexpo2026.domain.member.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

		@NotBlank
		String nickname,

		@NotBlank
		@JsonProperty("password_hash")
		String passwordHash
) {
}
