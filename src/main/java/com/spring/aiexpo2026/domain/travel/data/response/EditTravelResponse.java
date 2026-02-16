package com.spring.aiexpo2026.domain.travel.data.response;

public record EditTravelResponse(
		String message
) {

	public static EditTravelResponse of(String message) {
		return new EditTravelResponse(message);
	}
}
