package com.spring.aiexpo2026.domain.travel.controller;

import com.spring.aiexpo2026.domain.travel.data.request.AttractionsHistoryRequest;
import com.spring.aiexpo2026.domain.travel.data.request.EditTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.EditTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.FinishTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.TravelHistoryResponse;
import com.spring.aiexpo2026.domain.travel.service.TravelService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelController {

	private final TravelService travelService;

	// 여행 시작
	@PostMapping("/start")
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
														@Valid @RequestBody StartTravelRequest startTravelRequest) {
		return travelService.startTravel(servletRequest, startTravelRequest);
	}

	// 여행 종료
	@PutMapping("/finish")
	public ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest) {
		return travelService.finishTravel(servletRequest);
	}

	// 여행 수정
	@PutMapping("/edit/{id}")
	public ApiResponse<EditTravelResponse> editTravel(HttpServletRequest servletRequest,
													  @PathVariable Long id,
													  @Valid @RequestBody EditTravelRequest editTravelRequest) {
		return travelService.editTravel(
				servletRequest,
				id,
				editTravelRequest
		);
	}

	// 누적 여행 기록 조회
	@GetMapping("/my")
	public ApiResponse<List<TravelHistoryResponse>> travelHistory(HttpServletRequest httpServletRequest) {
		return travelService.travelHistory(httpServletRequest);
	}

	@PostMapping("/{travelId}/attractions")
	public ApiResponse<?> attractionsHistory(HttpServletRequest httpServletRequest,
											 @PathVariable Long travelId,
											 @RequestPart("request") AttractionsHistoryRequest attractionsHistoryRequest,
											 @RequestPart("file") MultipartFile multipartFile) {

		return travelService.attractionsHistory(
				httpServletRequest,
				travelId,
				attractionsHistoryRequest,
				multipartFile
		);
	}
}
