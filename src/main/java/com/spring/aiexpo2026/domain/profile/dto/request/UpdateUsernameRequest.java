package com.spring.aiexpo2026.domain.profile.dto.request;

public record UpdateUsernameRequest(
		String newUsername,
		String password
) {
}
