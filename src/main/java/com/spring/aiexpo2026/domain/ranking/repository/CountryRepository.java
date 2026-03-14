package com.spring.aiexpo2026.domain.ranking.repository;

import com.spring.aiexpo2026.domain.ranking.entity.Country;
import com.spring.aiexpo2026.domain.ranking.entity.CountryTheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findByCountryTheme(CountryTheme countryTheme, Pageable pageable);
}