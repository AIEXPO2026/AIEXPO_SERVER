package com.spring.aiexpo2026.domain.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.travel.data.request.AttractionsHistoryRequest;
import com.spring.aiexpo2026.domain.travel.data.request.EditTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.request.StartTravelRequest;
import com.spring.aiexpo2026.domain.travel.data.response.AttractionsResponse;
import com.spring.aiexpo2026.domain.travel.data.response.EditTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.FinishTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.GetAttractionsResponse;
import com.spring.aiexpo2026.domain.travel.data.response.StartTravelResponse;
import com.spring.aiexpo2026.domain.travel.data.response.TravelHistoryResponse;
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

import java.io.IOException;
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
	private final ObjectMapper objectMapper;

	@Value("${file.dir}")
	private String directory;

	@Override
	@Transactional
	public ApiResponse<StartTravelResponse> startTravel(
			HttpServletRequest servletRequest,
			StartTravelRequest startTravelRequest
	) {
		Member member = getMemberFromToken(servletRequest);

		TravelStatus status = validDateAndSetTravelStatus(
				member.getId(),
				startTravelRequest.startDate(),
				startTravelRequest.endDate()
		);

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

		String message = status == TravelStatus.TRAVEL_PLANNED
				? "여행이 계획되었습니다."
				: "여행이 시작되었습니다.";

		return ApiResponse.ok(StartTravelResponse.of(message));
	}

	@Override
	@Transactional
	public ApiResponse<FinishTravelResponse> finishTravel(HttpServletRequest servletRequest) {
		Member member = getMemberFromToken(servletRequest);

		Travel travel = travelRepository.findByMemberAndTravelStatus(member, TravelStatus.TRAVELING)
				.orElseThrow(() -> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL_TO_FINISH));

		travel.updateEndDate(LocalDate.now());
		travel.updateTravelStatus(TravelStatus.TRAVEL_FINISHED);

		return ApiResponse.ok(FinishTravelResponse.of("여행이 종료되었습니다."));
	}

	@Override
	@Transactional
	public ApiResponse<EditTravelResponse> editTravel(
			HttpServletRequest servletRequest,
			Long id,
			EditTravelRequest editTravelRequest
	) {
		Member member = getMemberFromToken(servletRequest);

		Travel travel = travelRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

		validateTravelOwner(travel, member);

		if (travel.getTravelStatus() != TravelStatus.TRAVEL_FINISHED) {
			throw new ApplicationException(TravelStatusCode.CANNOT_EDIT_TRAVEL);
		}

		travel.updatePeopleCount(editTravelRequest.peopleCount());
		travel.updateMood(editTravelRequest.mood());
		travel.updateAvgWeather(editTravelRequest.avgWeather());
		travel.updatePublicTravel(editTravelRequest.publicTravel());

		return ApiResponse.ok(EditTravelResponse.of("여행을 수정했습니다."));
	}

	@Override
	@Transactional(readOnly = true)
	public ApiResponse<List<TravelHistoryResponse>> travelHistory(HttpServletRequest servletRequest) {
		Member member = getMemberFromToken(servletRequest);

		List<TravelHistoryResponse> result = travelRepository.findByMember(member)
				.stream()
				.map(TravelHistoryResponse::from)
				.toList();

		return ApiResponse.ok(result);
	}

	@Override
	@Transactional
	public ApiResponse<AttractionsResponse> attractionsHistory(
			HttpServletRequest httpServletRequest,
			Long travelId,
			String requestJson,
			MultipartFile multipartFile
	) {
		try {
			AttractionsHistoryRequest attractionsHistoryRequest =
					objectMapper.readValue(requestJson, AttractionsHistoryRequest.class);

			Member member = getMemberFromToken(httpServletRequest);

			Travel travel = travelRepository.findById(travelId)
					.orElseThrow(() -> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

			validateTravelOwner(travel, member);

			String photoUrl = null;

			if (multipartFile != null && !multipartFile.isEmpty()) {
				Path uploadDirectory = Paths.get(directory).toAbsolutePath().normalize();
				if (!Files.exists(uploadDirectory)) {
					Files.createDirectories(uploadDirectory);
				}

				String rawName = StringUtils.cleanPath(
						Objects.requireNonNull(multipartFile.getOriginalFilename())
				);
				String fileName = UUID.randomUUID() + "_" + rawName;

				Path targetPath = uploadDirectory.resolve(fileName).normalize();
				if (!targetPath.startsWith(uploadDirectory)) {
					throw new ApplicationException(TravelStatusCode.FILE_UPLOAD_FAILED);
				}

				multipartFile.transferTo(targetPath.toFile());
				photoUrl = "/photos/" + fileName;
			}

			Attractions attractions = Attractions.builder()
					.travel(travel)
					.duration(attractionsHistoryRequest.duration())
					.detail(attractionsHistoryRequest.detail())
					.photoURL(photoUrl)
					.createdAt(LocalDateTime.now())
					.build();

			attractionsRepository.save(attractions);

			return ApiResponse.ok(AttractionsResponse.of("저장했습니다."));
		} catch (ApplicationException e) {
			throw e;
		} catch (JsonProcessingException e) {
			throw new ApplicationException(TravelStatusCode.INVALID_ATTRACTION_REQUEST);
		} catch (IOException e) {
			throw new ApplicationException(TravelStatusCode.FILE_UPLOAD_FAILED);
		} catch (Exception e) {
			throw new ApplicationException(TravelStatusCode.UNKNOWN_ERROR);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetAttractionsResponse> getAttractionsHistory(
			HttpServletRequest httpServletRequest,
			Long travelId
	) {
		Member member = getMemberFromToken(httpServletRequest);

		Travel travel = travelRepository.findById(travelId)
				.orElseThrow(() -> new ApplicationException(TravelStatusCode.CANNOT_FIND_TRAVEL));

		validateTravelOwner(travel, member);

		return attractionsRepository.findByTravelIdOrderByCreatedAtDesc(travelId)
				.stream()
				.map(GetAttractionsResponse::from)
				.toList();
	}

	private Member getMemberFromToken(HttpServletRequest servletRequest) {
		String token = jwtProvider.resolveToken(servletRequest);

		if (token == null || !jwtProvider.validateToken(token)) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}

		String nickname = jwtProvider.getNickname(token);

		return memberRepository.findByNickname(nickname)
				.orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));
	}

	private void validateTravelOwner(Travel travel, Member member) {
		if (!travel.getMember().getId().equals(member.getId())) {
			throw new ApplicationException(TravelStatusCode.FORBIDDEN_TRAVEL_ACCESS);
		}
	}

	private TravelStatus validDateAndSetTravelStatus(
			Long memberId,
			LocalDate startDate,
			LocalDate endDate
	) {
		LocalDate today = LocalDate.now();

		if (endDate.isBefore(startDate)) {
			throw new ApplicationException(TravelStatusCode.END_DATE_BEFORE_START_DATE);
		}

		travelRepository.findOverlappingActiveTravel(memberId, startDate, endDate)
				.ifPresent(travel -> {
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