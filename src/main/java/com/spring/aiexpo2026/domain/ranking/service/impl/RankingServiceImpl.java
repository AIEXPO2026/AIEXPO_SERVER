package com.spring.aiexpo2026.domain.ranking.service.impl;

import com.spring.aiexpo2026.domain.ranking.entity.Country;
import com.spring.aiexpo2026.domain.ranking.entity.CountryTheme;
import com.spring.aiexpo2026.domain.ranking.entity.SortType;
import com.spring.aiexpo2026.domain.ranking.repository.CountryRepository;
import com.spring.aiexpo2026.domain.ranking.service.RankingService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final CountryRepository countryRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<Country>> getCountryRanking(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(
                Sort.Order.desc("rating"),
                Sort.Order.desc("visitCount")
        ));

        Page<Country> rankings = countryRepository.findAll(pageable);

        return ApiResponse.ok(rankings);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<Country>> getCountry(CountryTheme countryTheme, SortType sortType, int page) {
        Sort sort = switch (sortType) {
            case POPULAR -> Sort.by(Sort.Order.desc("rating"));
            case NAME -> Sort.by(Sort.Order.asc("name"));
            case DEFAULT -> Sort.by(Sort.Order.asc("id"));
        };

        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<Country> countries = verityCountry(countryTheme, pageable);

        return ApiResponse.ok(countries);
    }

    private Page<Country> verityCountry(CountryTheme countryTheme, Pageable pageable) {
        if (countryTheme == CountryTheme.ALL) {
            return countryRepository.findAll(pageable);
        } else {
            return countryRepository.findByCountryTheme(countryTheme, pageable);
        }
    }
}