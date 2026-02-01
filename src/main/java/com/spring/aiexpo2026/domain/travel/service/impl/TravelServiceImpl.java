package com.spring.aiexpo2026.domain.travel.service.impl;

import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.exception.MemberStatusCode;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
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

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

	private final JwtProvider jwtProvider;

	private final TravelRepository travelRepository;
	private final MemberRepository memberRepository;

	@Override
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest) {
		Member member = getUsernameFromToken(servletRequest);

		if (travelRepository.existsByMemberIdAndTravelStatus(member.getId(), TravelStatus.TRAVELING)) {
			throw new ApplicationException(TravelStatusCode.TRAVEL_ALREADY_STARTED);
		}

		Travel travel = Travel.builder()
				.travelStatus(TravelStatus.TRAVELING)
				.startAt(LocalDate.now())
				.member(member)
				.build();

		travelRepository.save(travel);

		return ApiResponse.ok(StartTravelResponse.ok());
	}

	// todo: 아래에서 토큰이 null이 들어감..
	public Member getUsernameFromToken(HttpServletRequest servletRequest) {
		String token = jwtProvider.resolveToken(servletRequest);
		if (token == null || !jwtProvider.validateToken(token)) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}

		String username = jwtProvider.getUsername(token);

		return memberRepository.findByUsername(username).orElseThrow(()
				-> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));
	}
}

// 도망쳐도 괜찮아 가끔은
// 개운해지는 길일지도 몰라
// 걸어갈 방향이 그저 달라졌을 뿐이야 나아갈 맘이 중요하니까 자~
// 윷을 던져보자
// 모두 같이 놀자