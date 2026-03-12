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
	CANNOT_FIND_TRAVEL("CANNOT_FIND_TRAVEL", "요청한 여행을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	CANNOT_EDIT_TRAVEL("CANNOT_EDIT_TRAVEL", "여행을 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),
	WRONG_START_DATE("WRONG_START_DATE", "여행 시작일은 오늘 이후여야 합니다.", HttpStatus.BAD_REQUEST),
	END_DATE_BEFORE_START_DATE("END_DATE_BEFORE_START_DATE", "여행 종료일은 시작일보다 빠를 수 없습니다.", HttpStatus.BAD_REQUEST),

	FORBIDDEN_TRAVEL_ACCESS("FORBIDDEN_TRAVEL_ACCESS", "해당 여행에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
	INVALID_ATTRACTION_REQUEST("INVALID_ATTRACTION_REQUEST", "관광지 요청 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	FILE_UPLOAD_FAILED("FILE_UPLOAD_FAILED", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	UNKNOWN_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}