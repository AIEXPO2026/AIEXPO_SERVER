package com.spring.aiexpo2026.domain.travel.service.impl;

import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.FinishTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.entity.TravelStatus;
import com.spring.aiexpo2026.domain.travel.exception.TravelStatusCode;
import com.spring.aiexpo2026.domain.travel.repository.TravelRepository;
import com.spring.aiexpo2026.domain.travel.service.TravelService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

	private final JwtProvider jwtProvider;

	private final TravelRepository travelRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
														StartTravelRequest startTravelRequest) {
		Member member = getUsernameFromToken(servletRequest);

		TravelStatus status = validDateAndSetTravelStatus(member.getId(),
				startTravelRequest.startDate(),
				startTravelRequest.endDate());

		Travel travel = Travel.builder()
				.member(member)
				.budgetMin(startTravelRequest.budgetMin())
				.budgetMax(startTravelRequest.budgetMax())
				.startDate(startTravelRequest.startDate())
				.endDate(startTravelRequest.endDate())
				.peopleCount(startTravelRequest.peopleCount())
				.createdAt(LocalDateTime.now())
				.travelStatus(status)
				.build();
		travelRepository.save(travel);

		return ApiResponse.ok(StartTravelResponse.of(
				status == (TravelStatus.TRAVEL_PLANNED) ? "여행이 계획되었습니다." : "여행이 시작되었습니다."
		));
	}

	@Override
	@Transactional
	public ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest) {
		Member member = getUsernameFromToken(servletRequest);

		LocalDate today = LocalDate.now();

		Travel travel = travelRepository.findByMemberAndTravelStatus(member, TravelStatus.TRAVELING)
				.orElseThrow(() ->
					new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL_TO_FINISH)
				);

		travel.updateEndDate(today);
		travel.updateTravelStatus(TravelStatus.TRAVEL_FINISHED);

		return ApiResponse.ok(FinishTravelResponse.of("여행이 종료되었습니다."));
	}

	public Member getUsernameFromToken(HttpServletRequest servletRequest) {
		String token = jwtProvider.resolveToken(servletRequest);
		if (token == null || !jwtProvider.validateToken(token)) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}

		String nickname = jwtProvider.getNickname(token);

		return memberRepository.findByNickname(nickname).orElseThrow(()
				-> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));
	}

	private TravelStatus validDateAndSetTravelStatus(Long memberId,
													 LocalDate startDate,
													 LocalDate endDate) {

		LocalDate today = LocalDate.now();

		if (endDate.isBefore(startDate)) {
			throw new ApplicationException(TravelStatusCode.WRONG_END_DATE);
		}

		travelRepository.findOverlappingTravel(
				memberId,
				startDate,
				endDate
				).ifPresent(travel -> {
					throw new ApplicationException(
							travel.getTravelStatus() == TravelStatus.TRAVELING
							? TravelStatusCode.TRAVEL_ALREADY_STARTED
							: TravelStatusCode.TRAVEL_ALREADY_PLANNED
					);
		});

		if (startDate.isBefore(today)) {
			throw new ApplicationException(TravelStatusCode.WRONG_START_DATE);
		}

		return startDate.isEqual(today) ? TravelStatus.TRAVELING : TravelStatus.TRAVEL_PLANNED;
	}
}