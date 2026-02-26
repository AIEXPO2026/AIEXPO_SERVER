package com.spring.aiexpo2026.domain.travel.data.response;

import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.entity.TravelStatus;

import java.time.LocalDate;

public record TravelHistoryResponse(
		Long id,

		LocalDate startDate,

		LocalDate endDate,

		int budgetMin,

		int budgetMax,

		int peopleCount,

		int mood,

		String avgWeather,

		boolean publicTravel,

		TravelStatus travelStatus
) {

	public static TravelHistoryResponse from(Travel travel) {
		return new TravelHistoryResponse(
				travel.getId(),
				travel.getStartDate(),
				travel.getEndDate(),
				travel.getBudgetMin(),
				travel.getBudgetMax(),
				travel.getPeopleCount(),
				travel.getMood(),
				travel.getAvgWeather(),
				travel.isPublicTravel(),
				travel.getTravelStatus()
		);
	}
}
