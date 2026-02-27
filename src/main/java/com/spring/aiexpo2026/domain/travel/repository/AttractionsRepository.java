package com.spring.aiexpo2026.domain.travel.repository;

import com.spring.aiexpo2026.domain.travel.entity.Attractions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionsRepository extends JpaRepository<Attractions, Long> {

	List<Attractions> findByTravelIdOrderByCreatedAtDesc(Long travelId);
}
