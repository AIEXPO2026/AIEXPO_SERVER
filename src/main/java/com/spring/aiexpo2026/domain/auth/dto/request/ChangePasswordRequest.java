package com.spring.aiexpo2026.domain.auth.dto.request;

import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(

		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
				message = "비밀번호는 8자 이상이여야 하며, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
		String oldPassword,

		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
				message = "비밀번호는 8자 이상이여야 하며, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
		String newPassword
) {
}
