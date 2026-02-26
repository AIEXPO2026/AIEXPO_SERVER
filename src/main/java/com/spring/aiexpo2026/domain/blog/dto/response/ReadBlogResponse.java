package com.spring.aiexpo2026.domain.blog.dto.response;

import com.spring.aiexpo2026.domain.blog.entity.Blog;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ReadBlogResponse(
        Long id,
        String title,
        String author,
        LocalDate date,
        String country
) {
    public static ReadBlogResponse of(Blog blog) {
        return new ReadBlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getMember().getNickname(),
                blog.getDate(),
                blog.getCountry()
        );
    }

    public static List<ReadBlogResponse> fromList(Page<Blog> blogPage) {
        return blogPage.getContent().stream().map(
                blog -> ReadBlogResponse.of(blog)
        ).collect(Collectors.toList());
    }
}
