package com.spring.aiexpo2026.domain.member.entity;

import com.spring.aiexpo2026.domain.travel.entity.Travel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	private String email;

	@Column(name = "password_hash")
	private String passwordHash;

	@Column(unique = true)
	private String nickname;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Travel> travels = new ArrayList<>();

	public void updatePassword(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}