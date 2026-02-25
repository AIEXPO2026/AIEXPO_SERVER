package com.spring.aiexpo2026.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequest(

		@Email
		@NotBlank(message = "이메일을 입력해주세요.")
		String email
) {
}
