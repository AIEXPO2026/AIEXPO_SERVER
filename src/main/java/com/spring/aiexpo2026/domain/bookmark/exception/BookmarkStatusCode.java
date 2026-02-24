package com.spring.aiexpo2026.domain.bookmark.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookmarkStatusCode implements StatusCode {
    CANNOT_FIND_Bookmark("CANNOT_FIND_BOOKMARK", "북마크를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
