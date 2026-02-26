package com.spring.aiexpo2026.domain.travel.service.impl;

import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.travel.data.request.AttractionsHistoryRequest;
import com.spring.aiexpo2026.domain.travel.data.request.EditTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.*;
import com.spring.aiexpo2026.domain.travel.entity.Attractions;
import com.spring.aiexpo2026.domain.travel.entity.Travel;
import com.spring.aiexpo2026.domain.travel.entity.TravelStatus;
import com.spring.aiexpo2026.domain.travel.exception.TravelStatusCode;
import com.spring.aiexpo2026.domain.travel.repository.AttractionsRepository;
import com.spring.aiexpo2026.domain.travel.repository.TravelRepository;
import com.spring.aiexpo2026.domain.travel.service.TravelService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

	private final JwtProvider jwtProvider;

	private final TravelRepository travelRepository;
	private final MemberRepository memberRepository;
	private final AttractionsRepository attractionsRepository;

	@Value("${file.dir}")
	String directory;

	@Override
	@Transactional
	public ApiResponse<StartTravelResponse> startTravel(HttpServletRequest servletRequest,
														StartTravelRequest startTravelRequest) {
		Member member = getNicknameFromToken(servletRequest);

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
		Member member = getNicknameFromToken(servletRequest);

		LocalDate today = LocalDate.now();

		Travel travel = travelRepository.findByMemberAndTravelStatus(member, TravelStatus.TRAVELING)
				.orElseThrow(() ->
					new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL_TO_FINISH)
				);

		travel.updateEndDate(today);
		travel.updateTravelStatus(TravelStatus.TRAVEL_FINISHED);

		return ApiResponse.ok(FinishTravelResponse.of("여행이 종료되었습니다."));
	}

	@Override
	@Transactional
	public ApiResponse<EditTravelResponse> editTravel(HttpServletRequest servletRequest,
													  Long id,
													  EditTravelRequest editTravelRequest) {
		getNicknameFromToken(servletRequest);

		Travel travel = travelRepository.findById(id).orElseThrow(()
				-> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

		if (travel.getTravelStatus() == TravelStatus.TRAVEL_FINISHED) {
			travel.updatePeopleCount(editTravelRequest.peopleCount());
			travel.updateMood(editTravelRequest.mood());
			travel.updateAvgWeather(editTravelRequest.avgWeather());
			travel.updatePublicTravel(editTravelRequest.publicTravel());

			return ApiResponse.ok(EditTravelResponse.of("여행을 수정했습니다."));
		}

		throw new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL);
	}

	@Override
	public ApiResponse<List<TravelHistoryResponse>> travelHistory(HttpServletRequest servletRequest) {
		Member member = getNicknameFromToken(servletRequest);

		return ApiResponse.ok(travelRepository.findByMember(member)
				.stream()
				.map(TravelHistoryResponse::from)
				.toList());
	}

	@Override
	@Transactional
	public ApiResponse<AttractionsResponse> attractionsHistory(HttpServletRequest httpServletRequest,
											 Long travelId,
											 AttractionsHistoryRequest attractionsHistoryRequest,
											 MultipartFile multipartFile) {
		try {
			Member member = getNicknameFromToken(httpServletRequest);

			Travel travel = travelRepository.findById(travelId)
					.orElseThrow(() -> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

			if (!travel.getMember().getId().equals(member.getId())) {
				throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
			}

			Path setDirectory = Paths.get(directory).toAbsolutePath().normalize();
			if (!Files.exists(setDirectory)) {
				Files.createDirectories(setDirectory);
			}

			String rawName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
			String fileName = UUID.randomUUID() + "_" + rawName;

			Path targetPath = setDirectory.resolve(fileName).normalize();

			if (!targetPath.startsWith(setDirectory)) {
				throw new ApplicationException(TravelStatusCode.UNKNOWN_ERROR);
			}

			multipartFile.transferTo(targetPath.toFile());

			String photoURL = "/photos/" + fileName;

			Attractions attractions = Attractions.builder()
					.travel(travel)
					.duration(attractionsHistoryRequest.duration())
					.photoURL(photoURL)
					.detail(attractionsHistoryRequest.detail())
					.createdAt(LocalDateTime.now())
					.build();

			attractionsRepository.save(attractions);

			return ApiResponse.ok(AttractionsResponse.of("저장했습니다."));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<GetAttractionsResponse> getAttractionsHistory(HttpServletRequest httpServletRequest,
															  Long travelId) {
		Member member = getNicknameFromToken(httpServletRequest);

		Travel travel = travelRepository.findById(travelId).orElseThrow(()
				-> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

		if (!travel.getMember().getId().equals(member.getId())) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}

		return attractionsRepository.findByTravelIdOrderByCreatedAtDesc(travelId)
				.stream()
				.map(GetAttractionsResponse::from)
				.toList();
	}

	public Member getNicknameFromToken(HttpServletRequest servletRequest) {
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

		travelRepository.findOverlappingActiveTravel(
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