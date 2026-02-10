package com.spring.aiexpo2026.domain.travel.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TravelStatusCode implements StatusCode {

	TRAVEL_ALREADY_STARTED("TRAVEL_ALREADY_STARTED", "이미 여행중입니다.", HttpStatus.BAD_REQUEST),
	WRONG_START_DATE("WRONG_START_DATE", "여행 시작일은 오늘 이전이여야 합니다.", HttpStatus.BAD_REQUEST),
	WRONG_END_DATE("WRONG_END_DATE", "여행 종료일은 오늘 이후이여야 합니다.", HttpStatus.BAD_REQUEST);


	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
