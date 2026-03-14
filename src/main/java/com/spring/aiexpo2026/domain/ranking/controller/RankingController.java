package com.spring.aiexpo2026.domain.ranking.controller;

import com.spring.aiexpo2026.domain.ranking.entity.Country;
import com.spring.aiexpo2026.domain.ranking.entity.CountryTheme;
import com.spring.aiexpo2026.domain.ranking.entity.SortType;
import com.spring.aiexpo2026.domain.ranking.service.RankingService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("ranking")
    public ApiResponse<Page<Country>> getCountryRanking(@RequestParam(defaultValue = "0") int page) {
        return rankingService.getCountryRanking(page);
    }

    @GetMapping
    public ApiResponse<Page<Country>> getCountry(
            @RequestParam(defaultValue = "ALL") CountryTheme countryTheme,
            @RequestParam(defaultValue = "DEFAULT") SortType sortType,
            @RequestParam(defaultValue = "0") int page) {
        return rankingService.getCountry(countryTheme, sortType, page);
    }
}