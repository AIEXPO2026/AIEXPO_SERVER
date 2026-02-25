package com.spring.aiexpo2026.domain.blog.entity;

import com.spring.aiexpo2026.domain.blog.dto.request.UpdateBlogRequest;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    private Member member;
    @Builder.Default
    private int views = 0;
    private LocalDate date;
    private String country;
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    public void update(UpdateBlogRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.country = request.country();
        this.date = request.date();
    }

    public void plusView() {
        this.views++;
    }
}
