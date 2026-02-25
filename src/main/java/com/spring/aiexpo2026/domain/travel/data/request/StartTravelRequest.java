package com.spring.aiexpo2026.domain.travel.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StartTravelRequest(

		@Min(1000)
		@JsonProperty("budget_min")
		int budgetMin,

		@Min(2147483647)
		@JsonProperty("budget_max")
		int budgetMax,

		@NotNull
		@JsonProperty("start_date")
		LocalDate startDate,

		@NotNull
		@JsonProperty("end_date")
		LocalDate endDate,

		@Min(1)
		@JsonProperty("people_count")
		int peopleCount
) {
}
