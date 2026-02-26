package com.spring.aiexpo2026.domain.travel.service;

import com.spring.aiexpo2026.domain.travel.data.request.AttractionsHistoryRequest;
import com.spring.aiexpo2026.domain.travel.data.request.EditTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.*;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelService {

	ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
												 StartTravelRequest startTravelRequest);

	ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest);

	ApiResponse<EditTravelResponse> editTravel(HttpServletRequest servletRequest,
											   Long id,
											   EditTravelRequest editTravelRequest);

	ApiResponse<List<TravelHistoryResponse>> travelHistory(HttpServletRequest servletRequest);

	ApiResponse<AttractionsResponse> attractionsHistory(HttpServletRequest httpServletRequest,
														Long travelId,
														AttractionsHistoryRequest attractionsHistoryRequest,
														MultipartFile multipartFile);
}
