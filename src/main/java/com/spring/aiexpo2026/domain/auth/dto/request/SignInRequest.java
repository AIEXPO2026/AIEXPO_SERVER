package com.spring.aiexpo2026.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

		@NotBlank(message = "사용자명을 입력해주세요.")
		String nickname,

		@NotBlank(message = "비밀번호를 입력해주세요.")
		@JsonProperty("password_hash")
		String passwordHash
) {
}
