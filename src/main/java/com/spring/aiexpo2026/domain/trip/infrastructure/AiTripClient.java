package com.spring.aiexpo2026.domain.trip.infrastructure;

import com.spring.aiexpo2026.domain.trip.dto.request.AiCustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.DailyPlanRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.SuperSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.ThemeSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.response.CourseResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.DailyPlanResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.TravelSearchResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.exception.statuscode.CommonStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AiTripClient {

    private final WebClient aiWebClient;

    public TravelSearchResponse superSearch(SuperSearchRequest request) {
        return aiWebClient.post()
                .uri("/search/super")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TravelSearchResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }

    public TravelSearchResponse themeSearch(ThemeSearchRequest request) {
        return aiWebClient.post()
                .uri("/search/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TravelSearchResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }

    public TravelSearchResponse recommend() {
        return aiWebClient.get()
                .uri("/recommend")
                .retrieve()
                .bodyToMono(TravelSearchResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }

    public DailyPlanResponse dailyPlan(DailyPlanRequest request) {
        return aiWebClient.post()
                .uri("/plan/daily")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DailyPlanResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }

    public CourseResponse createCourse(CourseRequest request) {
        return aiWebClient.post()
                .uri("/course/location")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CourseResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }

    public CourseResponse customizeCourse(Long memberId, AiCustomizeCourseRequest request) {
        return aiWebClient.post()
                .uri("/course/customize/member/{memberId}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CourseResponse.class)
                .onErrorMap(e -> new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR))
                .block();
    }
}