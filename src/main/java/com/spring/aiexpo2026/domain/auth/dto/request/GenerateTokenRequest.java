package com.spring.aiexpo2026.domain.auth.dto.request;

import com.spring.aiexpo2026.domain.auth.entity.Role;
import jakarta.validation.constraints.NotBlank;

public record GenerateTokenRequest (

		@NotBlank(message = "토큰에 사용자명을 입력해주세요")
		String nickname,

		Role role
) {
}
