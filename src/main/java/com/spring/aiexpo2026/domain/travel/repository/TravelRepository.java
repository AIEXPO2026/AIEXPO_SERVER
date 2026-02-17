package com.spring.aiexpo2026.domain.travel.repository;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.entity.TravelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {

	@Query("""
	SELECT t
	FROM Travel t
	WHERE t.member.id = :memberId
	AND t.startDate <= :endDate
	AND t.endDate >= :startDate
	AND t.travelStatus != com.spring.aiexpo2026.domain.travel.entity.TravelStatus.TRAVEL_FINISHED
	ORDER BY t.id
	""")
	Optional<Travel> findOverlappingActiveTravel(
			@Param("memberId")
			Long memberId,

			@Param("startDate")
			LocalDate startDate,

			@Param("endDate")
			LocalDate endDate
	);

	Optional<Travel> findById(Long id);

	Optional<Travel> findByMemberAndTravelStatus(Member member, TravelStatus travelStatus);
}