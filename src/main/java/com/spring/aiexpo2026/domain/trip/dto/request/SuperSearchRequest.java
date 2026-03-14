package com.spring.aiexpo2026.domain.trip.dto.request;

public record SuperSearchRequest(
        String content,
        String country,
        String searchEngine
) {
}