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

	@Column(unique = true)
	private String username;

	private String password;

	@Email
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	private LocalDateTime timeStamp;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Travel> travels = new ArrayList<>();

	public void updatePassword(String password) {
		this.password = password;
	}
}