package com.spring.aiexpo2026.domain.blog.dto.response;

import com.spring.aiexpo2026.domain.blog.entity.Blog;

import java.time.LocalDate;

public record ViewBlogResponse(
        String title,
        String content,
        LocalDate date,
        String country,
        String author
) {
    public static ViewBlogResponse of(Blog blog) {
        return new ViewBlogResponse(
                blog.getTitle(),
                blog.getContent(),
                blog.getDate(),
                blog.getCountry(),
                blog.getMember().getUsername()
        );
    }
}
