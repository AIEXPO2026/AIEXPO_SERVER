package com.spring.aiexpo2026.domain.bookmark.service.impl;

import com.spring.aiexpo2026.domain.bookmark.data.response.BookmarkResponse;
import com.spring.aiexpo2026.domain.bookmark.exception.BookmarkStatusCode;
import com.spring.aiexpo2026.domain.bookmark.repository.BookmarkRepository;
import com.spring.aiexpo2026.domain.bookmark.service.BookmarkService;
import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.exception.MemberStatusCode;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BookmarkResponse>> getBookmarks(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));

        List<BookmarkResponse> bookmarks = bookmarkRepository.findByMember_Id(member.getId())
                .stream()
                .map(BookmarkResponse::from)
                .toList();

        return ApiResponse.ok(bookmarks);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteBookmark(String username, Long destinationId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));

        if (!bookmarkRepository.existsByMember_IdAndDestination_Id(member.getId(), destinationId)) {
            throw new ApplicationException(BookmarkStatusCode.CANNOT_FIND_Bookmark);
        }
        bookmarkRepository.deleteByMember_IdAndDestination_Id(member.getId(), destinationId);
        return ApiResponse.ok(null);
    }
}
