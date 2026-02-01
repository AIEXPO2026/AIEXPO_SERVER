package com.spring.aiexpo2026.domain.travel.data.response;

import org.springframework.http.HttpStatus;

public record StartTravelResponse(
		HttpStatus code,
		String message
) {

	public static StartTravelResponse ok() {
		return new StartTravelResponse(HttpStatus.OK, "여행을 시작했습니다.");
	}
}
