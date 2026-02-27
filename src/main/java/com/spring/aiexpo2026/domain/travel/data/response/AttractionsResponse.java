package com.spring.aiexpo2026.domain.travel.data.response;

public record AttractionsResponse(
		String message
) {

	public static AttractionsResponse of(String message) {
		return new AttractionsResponse(message);
	}
}
