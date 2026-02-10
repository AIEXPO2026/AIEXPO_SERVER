package com.spring.aiexpo2026.domain.travel.repository;

import com.spring.aiexpo2026.domain.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TravelRepository extends JpaRepository<Travel, Long> {

	boolean existsByMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long memberId,
											LocalDate start, LocalDate end);
}