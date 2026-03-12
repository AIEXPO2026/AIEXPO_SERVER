package com.spring.aiexpo2026.domain.trip.dto.response;

import java.util.List;

public record CourseResponse(
        List<CourseItem> course
) {
    public record CourseItem(
            int order,
            String place
    ) {
    }
}