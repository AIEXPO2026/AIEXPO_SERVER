package com.spring.aiexpo2026.global.exception.statuscode;

import org.springframework.http.HttpStatus;

public interface StatusCode {

	String getCode();
	String getMessage();
	HttpStatus getHttpStatus();
}
