package com.spring.aiexpo2026.domain.auth.entity;

import com.spring.aiexpo2026.domain.auth.dto.request.ChangePasswordRequest;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	@Column(name = "password_hash")
	private String passwordHash;

	@Column(unique = true)
	private String nickname;

	@Builder.Default
	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER_NOT_VERIFIED;

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Travel> travels = new ArrayList<>();

	public void changePassword(ChangePasswordRequest request,
							   PasswordEncoder passwordEncoder) {
		if (!passwordEncoder.matches(request.oldPassword(), this.passwordHash)) {
			throw new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS);
		}

		this.passwordHash = passwordEncoder.encode(request.newPassword());
	}

	public void updateRole(Role role) {
		this.role = role;
	}
}