package com.spring.aiexpo2026.domain.auth.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthStatusCode implements StatusCode {

	INVALID_TOKEN("INVALID_TOKEN", "잘못된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	CANNOT_GENERATE_TOKEN("CANNOT_GENERATE_TOKEN", "토큰을 발급할 수 없습니다.", HttpStatus.UNAUTHORIZED),
	INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
	ALREADY_LOGGED_OUT("ALREADY_LOGGED_OUT", "이미 로그아웃 되었습니다.", HttpStatus.BAD_REQUEST),

	USERNAME_ALREADY_EXIST("USERNAME_HAS_TAKEN", "이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST),
	CANNOT_FIND_EMAIL("CANNOT_FIND_EMAIL", "이메일이 존재 하지 않습니다.", HttpStatus.NOT_FOUND),
	EMAIL_ALREADY_EXIST("EMAIL_ALREADY_EXIST", "해당 이메일로 가입된 계정이 존재합니다.", HttpStatus.BAD_REQUEST),
	CANNOT_VERIFY_EMAIL("CANNOT_VERIFY_EMAIL", "이메일을 인증할 수 없습니다", HttpStatus.NOT_FOUND),
	USER_NOT_VERIFY("USER_NOT_VERIFY", "이메일 인증 후 서비스를 이용할 수 있습니다.", HttpStatus.BAD_REQUEST),
	PASSWORD_IS_WEAK("PASSWORD_IS_WEAK", "비밀번호 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	CANNOT_FIND_MEMBER("CANNOT_FIND_MEMBER", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
