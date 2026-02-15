package com.spring.aiexpo2026.domain.travel.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TravelStatusCode implements StatusCode {

	TRAVEL_ALREADY_STARTED("TRAVEL_ALREADY_STARTED", "이미 여행중입니다.", HttpStatus.BAD_REQUEST),
	TRAVEL_ALREADY_PLANNED("TRAVEL_ALREADY_PLANNED", "해당 날짜에 계획된 여행이 존재합니다.", HttpStatus.BAD_REQUEST),
	CANNOT_FIND_TRAVEL_TO_FINISH("CANNOT_FIND_TRAVEL_TO_FINISH", "종료할 여행을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	WRONG_START_DATE("WRONG_START_DATE", "여행 시작일은 오늘 이후여야 합니다.", HttpStatus.BAD_REQUEST),
	WRONG_END_DATE("WRONG_END_DATE", "여행 종료일은 오늘 이후여야 합니다.", HttpStatus.BAD_REQUEST);


	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
