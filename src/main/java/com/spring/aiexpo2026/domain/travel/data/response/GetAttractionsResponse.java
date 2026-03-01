package com.spring.aiexpo2026.domain.travel.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.aiexpo2026.domain.travel.entity.Attractions;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record GetAttractionsResponse(
		String photoURL,

		String detail,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		LocalTime duration,

		LocalDateTime createdAt
) {

	public static GetAttractionsResponse from(Attractions attractions) {
		return new GetAttractionsResponse(
				attractions.getPhotoURL(),
				attractions.getDetail(),
				attractions.getDuration(),
				attractions.getCreatedAt()
		);
	}
}
