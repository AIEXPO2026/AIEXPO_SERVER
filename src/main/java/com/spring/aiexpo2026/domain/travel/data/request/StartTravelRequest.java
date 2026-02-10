package com.spring.aiexpo2026.domain.travel.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StartTravelRequest(
		@NotNull
		@JsonProperty("budget_min")
		int budgetMin,

		@NotNull
		@JsonProperty("budget_max")
		int budgetMax,

		@NotNull
		@JsonProperty("start_date")
		LocalDate startDate,

		@NotNull
		@JsonProperty("end_date")
		LocalDate endDate,

		@NotNull
		@JsonProperty("people_count")
		int peopleCount
) {
}
