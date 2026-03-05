package com.spring.aiexpo2026.domain.blog.dto.request;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.blog.entity.Blog;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record WriteBlogRequest(
        @NotBlank(message = "title은 필수입력입니다.")
        String title,
        @NotBlank(message = "content은 필수입력입니다.")
        String content,
        LocalDate date,
        @NotBlank(message = "country은 필수입력입니다.")
        String country
) {
        public Blog toEntity(WriteBlogRequest request, Member member) {
                return Blog.builder()
                        .title(request.title())
                        .content(request.content)
                        .date(request.date)
                        .country(request.country)
                        .member(member)
                        .build();
        }
}
