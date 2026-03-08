package com.spring.aiexpo2026.domain.ranking.repository;

import com.spring.aiexpo2026.domain.ranking.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}