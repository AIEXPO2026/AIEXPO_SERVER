package com.spring.aiexpo2026.domain.trip.service;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.trip.dto.request.AiCustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.CustomizeCourseRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.DailyPlanRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.SuperSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.request.ThemeSearchRequest;
import com.spring.aiexpo2026.domain.trip.dto.response.CourseResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.DailyPlanResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.SavedCourseResponse;
import com.spring.aiexpo2026.domain.trip.dto.response.TravelSearchResponse;
import com.spring.aiexpo2026.domain.trip.infrastructure.AiTripClient;
import com.spring.aiexpo2026.domain.travel.entity.Attractions;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.repository.AttractionsRepository;
import com.spring.aiexpo2026.domain.travel.repository.TravelRepository;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final MemberRepository memberRepository;

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

    @Transactional
    public SavedCourseResponse createCourseAndSave(Long memberId, CourseRequest request) {
        CourseResponse aiResponse = aiTripClient.createCourse(request);
        Long travelId = saveCourse(memberId, aiResponse);
        return new SavedCourseResponse(travelId, aiResponse.course());
    }

    @Transactional
    public SavedCourseResponse customizeCourseAndSave(Long memberId, CustomizeCourseRequest request) {
        List<Travel> travels = travelRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

        if (travels.isEmpty()) {
            throw new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER);
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

        CourseResponse aiResponse = aiTripClient.customizeCourse(aiRequest);
        Long travelId = saveCourse(memberId, aiResponse);

        return new SavedCourseResponse(travelId, aiResponse.course());
    }

    @Transactional
    protected Long saveCourse(Long memberId, CourseResponse response) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        Travel travel = Travel.builder()
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        Travel savedTravel = travelRepository.save(travel);

        List<Attractions> attractions = response.course().stream()
                .map(item -> toAttraction(savedTravel, item))
                .toList();

        attractionsRepository.saveAll(attractions);

        return savedTravel.getId();
    }

    private Attractions toAttraction(Travel travel, CourseResponse.CourseItem item) {
        return Attractions.builder()
                .travel(travel)
                .detail(item.place())
                .createdAt(LocalDateTime.now())
                .build();
    }
}