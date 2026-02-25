package com.spring.aiexpo2026.domain.blog.dto.response;

public record MessageResponse(
        String message
) {
    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}
