package com.spring.aiexpo2026.domain.member.data.request;

public record ChangePasswordRequest(
		String username,

		String oldPassword,

		String newPassword
) {
}
