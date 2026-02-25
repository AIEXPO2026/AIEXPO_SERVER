package com.spring.aiexpo2026.domain.blog.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BlogStatusCode implements StatusCode {
    BLOG_NOT_FOUND("BLOG_NOT_FOUND", "브이로그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BLOG_UPDATE_FORBIDDEN("BLOG_UPDATE_FORBIDDEN", "수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
    BLOG_DELETE_FORBIDDEN("BLOG_DELETE_FORBIDDEN", "삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
