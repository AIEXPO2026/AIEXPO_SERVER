package com.spring.aiexpo2026.domain.travel.controller;

import com.spring.aiexpo2026.domain.travel.data.request.EditTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.AttractionsResponse;
import com.spring.aiexpo2026.domain.travel.data.response.EditTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.FinishTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.GetAttractionsResponse;
import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.TravelHistoryResponse;
import com.spring.aiexpo2026.domain.travel.service.TravelService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelController {

	private final TravelService travelService;

	@PostMapping("/start")
	public ApiResponse<StartTravelResponse> startTravel(
			HttpServletRequest servletRequest,
			@Valid @RequestBody StartTravelRequest startTravelRequest
	) {
		return travelService.startTravel(servletRequest, startTravelRequest);
	}

	@PutMapping("/finish")
	public ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest) {
		return travelService.finishTravel(servletRequest);
	}

	@PutMapping("/edit/{id}")
	public ApiResponse<EditTravelResponse> editTravel(
			HttpServletRequest servletRequest,
			@PathVariable Long id,
			@Valid @RequestBody EditTravelRequest editTravelRequest
	) {
		return travelService.editTravel(servletRequest, id, editTravelRequest);
	}

	@GetMapping("/my")
	public ApiResponse<List<TravelHistoryResponse>> travelHistory(HttpServletRequest httpServletRequest) {
		return travelService.travelHistory(httpServletRequest);
	}

	@PostMapping(value = "/{travelId}/attractions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<AttractionsResponse> attractionsHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable Long travelId,
			@RequestPart("request") String requestJson,
			@RequestPart(value = "file", required = false) MultipartFile multipartFile
	) {
		return travelService.attractionsHistory(
				httpServletRequest,
				travelId,
				requestJson,
				multipartFile
		);
	}

	@GetMapping("/{travelId}/attractions")
	public ApiResponse<List<GetAttractionsResponse>> getAttractionsHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable Long travelId
	) {
		return ApiResponse.ok(travelService.getAttractionsHistory(httpServletRequest, travelId));
	}
}