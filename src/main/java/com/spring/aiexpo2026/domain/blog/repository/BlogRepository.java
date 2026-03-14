package com.spring.aiexpo2026.domain.blog.repository;

import com.spring.aiexpo2026.domain.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findAll(Pageable pageable);
    void deleteByMemberId(Long memberId);
}
