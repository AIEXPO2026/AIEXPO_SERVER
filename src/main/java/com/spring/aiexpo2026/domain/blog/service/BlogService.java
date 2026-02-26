package com.spring.aiexpo2026.domain.blog.service;

import com.spring.aiexpo2026.domain.blog.dto.request.UpdateBlogRequest;
import com.spring.aiexpo2026.domain.blog.dto.request.WriteBlogRequest;
import com.spring.aiexpo2026.domain.blog.dto.response.MessageResponse;
import com.spring.aiexpo2026.domain.blog.dto.response.ReadBlogResponse;
import com.spring.aiexpo2026.domain.blog.dto.response.ViewBlogResponse;
import com.spring.aiexpo2026.domain.blog.entity.Blog;
import com.spring.aiexpo2026.domain.blog.exception.BlogStatusCode;
import com.spring.aiexpo2026.domain.blog.repository.BlogRepository;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.data.PageResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public ApiResponse<MessageResponse> writeBlog(WriteBlogRequest request) {
        Member member = securityUtil.getMember();
        blogRepository.save(Blog.builder()
                .title(request.title())
                .content(request.content())
                .country(request.country())
                .date(request.date())
                .member(member)
                .build());

        return ApiResponse.create(MessageResponse.of("글이 작성되었습니다."));
    }

    public PageResponse<ReadBlogResponse> readBlog(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");

        Page<Blog> blogPage = blogRepository.findAll(pageable);

        return PageResponse.of(ReadBlogResponse.fromList(blogPage), blogPage);
    }

    @Transactional
    public ApiResponse<ViewBlogResponse> viewBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(BlogStatusCode.BLOG_NOT_FOUND));
        blog.plusView();

        return ApiResponse.ok(ViewBlogResponse.of(blog));
    }

    @Transactional
    public ApiResponse<MessageResponse> updateBlog(UpdateBlogRequest request, Long id) {
        Member member = securityUtil.getMember();
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(BlogStatusCode.BLOG_NOT_FOUND));

        if (!member.equals(blog.getMember())) {
            throw new ApplicationException(BlogStatusCode.BLOG_UPDATE_FORBIDDEN);
        }

        blog.update(request);

        return ApiResponse.ok(MessageResponse.of("글이 수정되었습니다."));
    }

    @Transactional
    public ApiResponse<MessageResponse> deleteBlog(Long id) {
        Member member = securityUtil.getMember();
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(BlogStatusCode.BLOG_NOT_FOUND));

        if (!member.equals(blog.getMember())) {
            throw new ApplicationException(BlogStatusCode.BLOG_DELETE_FORBIDDEN);
        }

        blogRepository.delete(blog);

        return ApiResponse.ok(MessageResponse.of("글이 삭제되었습니다."));
    }
}
