package com.spring.aiexpo2026.domain.trip.dto.response;

import java.util.List;

public record TravelSearchResponse(
        List<TravelItem> results
) {
    public record TravelItem(
            String title,
            String content,
            String location,
            List<String> sources
    ) {
    }
}