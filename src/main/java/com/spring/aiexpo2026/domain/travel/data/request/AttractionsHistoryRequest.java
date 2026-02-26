package com.spring.aiexpo2026.domain.travel.data.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record AttractionsHistoryRequest(

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		LocalTime duration,

		String detail
) {
}