package com.spring.aiexpo2026.domain.ranking.service;

import com.spring.aiexpo2026.domain.ranking.entity.Country;
import com.spring.aiexpo2026.global.data.ApiResponse;
import org.springframework.data.domain.Page;

public interface RankingService {

    ApiResponse<Page<Country>> getCountryRanking(int page);
}
