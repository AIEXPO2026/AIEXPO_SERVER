package com.spring.aiexpo2026.domain.travel.repository;

import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.entity.TravelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {

	boolean existsByMemberIdAndTravelStatus(Long memberId, TravelStatus travelStatus);
}