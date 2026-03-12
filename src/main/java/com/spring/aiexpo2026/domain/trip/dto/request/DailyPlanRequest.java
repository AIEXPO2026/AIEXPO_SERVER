package com.spring.aiexpo2026.domain.trip.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyPlanRequest(
        String location,

        @JsonProperty("start_time")
        String startTime,

        @JsonProperty("end_time")
        String endTime


) {
}