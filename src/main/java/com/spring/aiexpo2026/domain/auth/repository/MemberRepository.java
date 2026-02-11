package com.spring.aiexpo2026.domain.auth.repository;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByNickname(String nickname);

	boolean existsByNickname(String nickname);
	boolean existsByEmail(String email);
}
