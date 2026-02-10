package com.spring.aiexpo2026.domain.auth.data.request;

import com.spring.aiexpo2026.domain.member.entity.Role;
import jakarta.validation.constraints.NotBlank;

public record GenerateTokenRequest (

		@NotBlank
		String nickname,

		Role role
) {
}
