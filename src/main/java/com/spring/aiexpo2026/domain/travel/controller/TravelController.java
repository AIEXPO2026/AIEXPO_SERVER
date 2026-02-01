package com.spring.aiexpo2026.domain.travel.controller;

import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.domain.travel.service.TravelService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelController {

	private final TravelService travelService;

	@PostMapping("/start")
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest) {
		return travelService.startTravel(servletRequest);
	}
}
