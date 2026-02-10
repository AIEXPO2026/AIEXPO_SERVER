package com.spring.aiexpo2026.domain.member.data.request;

public record ChangePasswordRequest(
		String nickname,

		String oldPassword,

		String newPassword
) {
}
