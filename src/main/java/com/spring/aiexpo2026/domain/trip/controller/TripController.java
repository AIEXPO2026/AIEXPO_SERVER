package com.spring.aiexpo2026.domain.trip.controller;

import com.spring.aiexpo2026.domain.trip.dto.request.CourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.DailyPlanRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.SuperSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.ThemeSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.response.CourseResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.DailyPlanResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.TravelSearchResponse;
import com.spring.aiexpo2026.domain.trip.service.TripService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/search/super")
    public ApiResponse<TravelSearchResponse> superSearch(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody SuperSearchRequest request
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.superSearch(memberId, request));
    }

    @PostMapping("/search/theme")
    public ApiResponse<TravelSearchResponse> themeSearch(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody ThemeSearchRequest request
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.themeSearch(memberId, request));
    }

    @GetMapping("/recommend")
    public ApiResponse<TravelSearchResponse> recommend(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.recommend(memberId));
    }

    @PostMapping("/plan/daily")
    public ApiResponse<DailyPlanResponse> dailyPlan(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody DailyPlanRequest request
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.dailyPlan(memberId, request));
    }

    @PostMapping("/course/location")
    public ApiResponse<CourseResponse> createCourse(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody CourseRequest request
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.createCourse(memberId, request));
    }

    @PostMapping("/course/customize")
    public ApiResponse<CourseResponse> customizeCourse(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody CustomizeCourseRequest request
    ) {
        Long memberId = memberDetails.getMember().getId();
        return ApiResponse.ok(tripService.customizeCourse(memberId, request));
    }
}