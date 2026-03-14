package com.spring.aiexpo2026.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangeNicknameRequest(
		@JsonProperty("new_nickname")
		String newNickname,

		@JsonProperty("password_hash")
		String passwordHash
) {
}
