package com.spring.aiexpo2026.domain.member.repository;

import com.spring.aiexpo2026.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUsername(String username);

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

	@Modifying
	@Query("UPDATE Member m SET m.credit = m.credit + :amount WHERE m.id = :id")
	void chargeCredit(@Param("id") Long id, @Param("amount") int amount);
}
