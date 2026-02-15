package com.spring.aiexpo2026.domain.travel.data.response;

public record StartTravelResponse(
		String message
) {

	public static StartTravelResponse of(String message) {
		return new StartTravelResponse(message);
	}
}
