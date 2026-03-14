package com.spring.aiexpo2026.domain.travel.data.response;

public record FinishTravelResponse(
		Long id,
		String message
) {

	public static FinishTravelResponse of(Long id,
										  String message) {
		return new FinishTravelResponse(id, message);
	}
}
