package com.spring.aiexpo2026.domain.travel.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EditTravelRequest(
		@Positive
		@JsonProperty("people_count")
		int peopleCount,

		@NotNull
		@Positive
		int mood,

		@NotBlank
		@JsonProperty("avg_weather")
		String avgWeather,

		@JsonProperty("public_travel")
		boolean publicTravel
) {
}
