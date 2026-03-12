package com.spring.aiexpo2026.domain.trip.service;

import com.spring.aiexpo2026.domain.trip.dto.request.AiCustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.DailyPlanRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.SuperSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.ThemeSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.response.CourseResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.DailyPlanResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.TravelSearchResponse;
import com.spring.aiexpo2026.domain.trip.infrastructure.AiTripClient;
import com.spring.aiexpo2026.domain.travel.entity.Attractions;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.repository.AttractionsRepository;
import com.spring.aiexpo2026.domain.travel.repository.TravelRepository;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.exception.statuscode.CommonStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final AiTripClient aiTripClient;
    private final TravelRepository travelRepository;
    private final AttractionsRepository attractionsRepository;

    public TravelSearchResponse superSearch(Long memberId, SuperSearchRequest request) {
        return aiTripClient.superSearch(request);
    }

    public TravelSearchResponse themeSearch(Long memberId, ThemeSearchRequest request) {
        return aiTripClient.themeSearch(request);
    }

    public TravelSearchResponse recommend(Long memberId) {
        return aiTripClient.recommend();
    }

    public DailyPlanResponse dailyPlan(Long memberId, DailyPlanRequest request) {
        return aiTripClient.dailyPlan(request);
    }

    public CourseResponse createCourse(Long memberId, CourseRequest request) {
        return aiTripClient.createCourse(request);
    }

    public CourseResponse customizeCourse(Long memberId, CustomizeCourseRequest request) {
        List<Travel> travels = travelRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

        if (travels.isEmpty()) {
            throw new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR);
        }

        List<Long> travelIds = travels.stream()
                .map(Travel::getId)
                .toList();

        List<Attractions> attractions = attractionsRepository.findByTravelIdInOrderByCreatedAtDesc(travelIds);

        List<String> savedPlaces = attractions.stream()
                .map(Attractions::getDetail)
                .filter(detail -> detail != null && !detail.isBlank())
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(LinkedHashSet::new),
                        List::copyOf
                ));

        if (savedPlaces.isEmpty()) {
            throw new ApplicationException(CommonStatusCode.INTERNAL_SERVER_ERROR);
        }

        List<Integer> moods = travels.stream()
                .map(Travel::getMood)
                .filter(Objects::nonNull)
                .toList();

        List<Integer> peopleCounts = travels.stream()
                .map(Travel::getPeopleCount)
                .filter(Objects::nonNull)
                .toList();

        List<String> avgWeathers = travels.stream()
                .map(Travel::getAvgWeather)
                .filter(weather -> weather != null && !weather.isBlank())
                .distinct()
                .toList();

        Integer budgetMin = travels.stream()
                .map(Travel::getBudgetMin)
                .filter(Objects::nonNull)
                .min(Integer::compareTo)
                .orElse(null);

        Integer budgetMax = travels.stream()
                .map(Travel::getBudgetMax)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(null);

        AiCustomizeCourseRequest aiRequest = new AiCustomizeCourseRequest(
                request.style(),
                savedPlaces,
                new AiCustomizeCourseRequest.TravelContext(
                        moods,
                        peopleCounts,
                        avgWeathers,
                        budgetMin,
                        budgetMax
                )
        );

        return aiTripClient.customizeCourse(memberId, aiRequest);
    }
}