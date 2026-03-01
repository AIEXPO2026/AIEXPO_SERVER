package com.spring.aiexpo2026.domain.blog.controller;

import com.spring.aiexpo2026.domain.blog.dto.request.UpdateBlogRequest;
import com.spring.aiexpo2026.domain.blog.dto.request.WriteBlogRequest;
import com.spring.aiexpo2026.domain.blog.dto.response.MessageResponse;
import com.spring.aiexpo2026.domain.blog.dto.response.ReadBlogResponse;
import com.spring.aiexpo2026.domain.blog.dto.response.ViewBlogResponse;
import com.spring.aiexpo2026.domain.blog.service.BlogService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.data.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping
    public ApiResponse<MessageResponse> writeBlog(
            @Valid @RequestBody WriteBlogRequest request) {
        return blogService.writeBlog(request);
    }

    @GetMapping
    public PageResponse<ReadBlogResponse> readBlog(
            @RequestParam(defaultValue = "0") int page) {
        return blogService.readBlog(page);
    }

    @GetMapping("/{id}")
    public ApiResponse<ViewBlogResponse> viewBlog(@PathVariable Long id) {
        return blogService.viewBlog(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<MessageResponse> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlogRequest request) {
        return blogService.updateBlog(request,id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<MessageResponse> deleteBlog(@PathVariable Long id) {
        return blogService.deleteBlog(id);
    }
}
