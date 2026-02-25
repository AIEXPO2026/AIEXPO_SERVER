package com.spring.aiexpo2026.domain.travel.data.response;

public record FinishTravelResponse(
		String message
) {

	public static FinishTravelResponse of(String message) {
		return new FinishTravelResponse(message);
	}
}
