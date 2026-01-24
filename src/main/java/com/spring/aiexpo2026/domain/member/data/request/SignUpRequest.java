package com.spring.aiexpo2026.domain.member.data.request;

import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SignUpRequest(

		@NotBlank
		String username,

		@NotBlank
		@Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하여야 합니다.")
		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
				message = "비밀번호는 8자 이상이여야 하며, 영문 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
		String password,

		@Email
		@NotBlank
		String email
) {

	public Member toEntity(String encodedPassword) {
		return Member.builder()
				.username(username)
				.password(encodedPassword)
				.email(email)
				.role(Role.USER)
				.timeStamp(LocalDateTime.now())
				.build();
	}
}
