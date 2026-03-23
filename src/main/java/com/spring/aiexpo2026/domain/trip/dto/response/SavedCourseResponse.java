package com.spring.aiexpo2026.domain.trip.dto.response;

import java.util.List;

public record SavedCourseResponse(
        Long travelId,
        List<CourseResponse.CourseItem> course
) {
}