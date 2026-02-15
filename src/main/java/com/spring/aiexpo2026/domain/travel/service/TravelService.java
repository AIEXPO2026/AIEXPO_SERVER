package com.spring.aiexpo2026.domain.travel.service;

import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.FinishTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TravelService {

	ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
												 StartTravelRequest startTravelRequest);

	ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest);
}
