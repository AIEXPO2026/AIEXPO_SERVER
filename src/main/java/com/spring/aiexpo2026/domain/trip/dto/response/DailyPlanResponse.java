package com.spring.aiexpo2026.domain.trip.dto.response;

import java.util.List;

public record DailyPlanResponse(
        List<PlanItem> plan
) {
    public record PlanItem(
            String time,
            String activity
    ) {
    }
}