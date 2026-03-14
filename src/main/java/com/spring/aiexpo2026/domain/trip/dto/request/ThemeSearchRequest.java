package com.spring.aiexpo2026.domain.trip.dto.request;

public record ThemeSearchRequest(
        String theme,
        String country,
        String searchEngine
) {
}
