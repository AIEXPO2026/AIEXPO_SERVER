package com.spring.aiexpo2026.domain.trip.dto.request;

import java.util.List;

public record AiCustomizeCourseRequest(
        String style,
        List<String> savedPlaces,
        TravelContext travelContext
) {
    public record TravelContext(
            List<Integer> moods,
            List<Integer> peopleCounts,
            List<String> avgWeathers,
            Integer budgetMin,
            Integer budgetMax
    ) {
    }
}