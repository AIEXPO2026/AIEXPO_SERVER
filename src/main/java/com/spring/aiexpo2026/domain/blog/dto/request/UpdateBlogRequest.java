package com.spring.aiexpo2026.domain.blog.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateBlogRequest(
        @NotBlank(message = "title은 필수입력입니다.")
        String title,
        @NotBlank(message = "content은 필수입력입니다.")
        String content,
        LocalDate date,
        @NotBlank(message = "country은 필수입력입니다.")
        String country
) {
}
