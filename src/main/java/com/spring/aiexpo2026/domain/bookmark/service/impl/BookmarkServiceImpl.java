package com.spring.aiexpo2026.domain.bookmark.service.impl;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.bookmark.data.request.AddBookmarkRequest;
import com.spring.aiexpo2026.domain.bookmark.data.response.BookmarkResponse;
import com.spring.aiexpo2026.domain.bookmark.entity.Bookmark;
import com.spring.aiexpo2026.domain.bookmark.exception.BookmarkStatusCode;
import com.spring.aiexpo2026.domain.bookmark.repository.BookmarkRepository;
import com.spring.aiexpo2026.domain.bookmark.service.BookmarkService;
import com.spring.aiexpo2026.domain.ranking.entity.Country;
import com.spring.aiexpo2026.domain.ranking.repository.CountryRepository;
import com.spring.aiexpo2026.domain.travel.entity.Destination;
import com.spring.aiexpo2026.domain.travel.exception.TravelStatusCode;
import com.spring.aiexpo2026.domain.travel.repository.DestinationRepository;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final DestinationRepository destinationRepository;
    private final CountryRepository countryRepository;

    @Override
    @Transactional
    public ApiResponse<Void> addBookmark(String nickname, AddBookmarkRequest request) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        Bookmark bookmark;

        if (request.getCountryId() != null) {
            if (bookmarkRepository.existsByMember_IdAndCountry_Id(member.getId(), request.getCountryId())) {
                throw new ApplicationException(BookmarkStatusCode.DESTINATION_ALREADY_BOOKMARKED);
            }
            Country country = countryRepository.findById(request.getCountryId())
                    .orElseThrow(() -> new ApplicationException(BookmarkStatusCode.CANNOT_FIND_BOOKMARK));

            bookmark = Bookmark.builder()
                    .member(member)
                    .country(country)
                    .createdAt(LocalDateTime.now())
                    .build();

        } else {
            if (bookmarkRepository.existsByMember_IdAndDestination_Id(member.getId(), request.getDestinationId())) {
                throw new ApplicationException(BookmarkStatusCode.DESTINATION_ALREADY_BOOKMARKED);
            }
            Destination destination = destinationRepository.findById(request.getDestinationId())
                    .orElseThrow(() -> new ApplicationException(BookmarkStatusCode.CANNOT_FIND_BOOKMARK));

            bookmark = Bookmark.builder()
                    .member(member)
                    .destination(destination)
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        bookmarkRepository.save(bookmark);
        return ApiResponse.ok(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BookmarkResponse>> getBookmarks(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        List<BookmarkResponse> bookmarks = bookmarkRepository.findByMember_Id(member.getId())
                .stream()
                .map(BookmarkResponse::from)
                .toList();

        return ApiResponse.ok(bookmarks);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteBookmark(String nickname, Long id, boolean isCountry) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        if (isCountry) {
            if (!bookmarkRepository.existsByMember_IdAndCountry_Id(member.getId(), id)) {
                throw new ApplicationException(BookmarkStatusCode.CANNOT_FIND_BOOKMARK);
            }
            bookmarkRepository.deleteByMember_IdAndCountry_Id(member.getId(), id);
        } else {
            if (!bookmarkRepository.existsByMember_IdAndDestination_Id(member.getId(), id)) {
                throw new ApplicationException(BookmarkStatusCode.CANNOT_FIND_BOOKMARK);
            }
            bookmarkRepository.deleteByMember_IdAndDestination_Id(member.getId(), id);
        }

        return ApiResponse.ok(null);
    }
}
