package com.spring.aiexpo2026.domain.travel.service.impl;

import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.exception.MemberStatusCode;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
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
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

	private final JwtProvider jwtProvider;

	private final TravelRepository travelRepository;
	private final MemberRepository memberRepository;

	@Override
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
														StartTravelRequest startTravelRequest) {
		Member member = getUsernameFromToken(servletRequest);
		LocalDate today = LocalDate.now();

		if (travelRepository.existsByMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(member.getId(), today, today)) {
			throw new ApplicationException(TravelStatusCode.TRAVEL_ALREADY_STARTED);
		}

		isValidPeriod(startTravelRequest.startDate(), startTravelRequest.endDate());

		Travel travel = Travel.builder()
				.member(member)
				.budgetMin(startTravelRequest.budgetMin())
				.budgetMax(startTravelRequest.budgetMax())
				.startDate(startTravelRequest.startDate())
				.endDate(startTravelRequest.endDate())
				.createdAt(LocalDateTime.now())
				.build();
		travelRepository.save(travel);

		return ApiResponse.ok(StartTravelResponse.ok());
	}

	public Member getUsernameFromToken(HttpServletRequest servletRequest) {
		String token = jwtProvider.resolveToken(servletRequest);
		if (token == null || !jwtProvider.validateToken(token)) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}

		String nickname = jwtProvider.getNickname(token);

		return memberRepository.findByNickname(nickname).orElseThrow(()
				-> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));
	}

	public void isValidPeriod(@NotNull LocalDate startDate,
							  @NotNull LocalDate endDate) {
		LocalDate today = LocalDate.now();

		if (endDate.isBefore(startDate)) {
			throw new ApplicationException(TravelStatusCode.WRONG_END_DATE);
		}

		if (startDate.isBefore(today)) {
			throw new ApplicationException(TravelStatusCode.WRONG_START_DATE);
		}

	}
}

// 도망쳐도 괜찮아 가끔은
// 개운해지는 길일지도 몰라
// 걸어갈 방향이 그저 달라졌을 뿐이야 나아갈 맘이 중요하니까 자~
// 윷을 던져보자
// 모두 같이 놀자