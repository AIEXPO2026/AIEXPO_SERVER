package com.spring.aiexpo2026.domain.bookmark.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookmarkStatusCode implements StatusCode {
    CANNOT_FIND_BOOKMARK("CANNOT_FIND_BOOKMARK", "북마크를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DESTINATION_ALREADY_BOOKMARKED("ALREADY_BOOKMARKED", "이미 북마크 했습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
