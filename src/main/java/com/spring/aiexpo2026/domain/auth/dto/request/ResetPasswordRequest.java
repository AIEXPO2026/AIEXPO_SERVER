package com.spring.aiexpo2026.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(

		@Email
		@NotBlank(message = "이메일을 입력해주세요.")
		String email,

		@NotBlank(message = "인증코드를 입력해주세요.")
		String authNum,

		@NotBlank
		@JsonProperty("password_hash")
		String passwordHash
) {
}
