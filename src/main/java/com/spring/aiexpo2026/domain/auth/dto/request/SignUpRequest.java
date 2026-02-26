package com.spring.aiexpo2026.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SignUpRequest(

		@NotBlank
		String name,

		@NotBlank
		String nickname,

		@NotBlank(message = "비밀번호를 입력해주세요")
		@JsonProperty("password_hash")
		@Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하여야 합니다.")
		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
				message = "비밀번호는 8자 이상이여야 하며, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
		String passwordHash,

		@Email
		@NotBlank(message = "이메일을 입력해주세요.")
		String email,

		@NotBlank(message = "인증코드를 입력해주세요.")
		String authNum
) {

	public Member toEntity(String encodedPassword) {
		return Member.builder()
				.name(name)
				.nickname(nickname)
				.passwordHash(encodedPassword)
				.email(email)
				.role(Role.USER)
				.createdAt(LocalDateTime.now())
				.build();
	}
}
