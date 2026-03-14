package com.spring.aiexpo2026.domain.bookmark.service;

import com.spring.aiexpo2026.domain.bookmark.data.request.AddBookmarkRequest;
import com.spring.aiexpo2026.domain.bookmark.data.response.BookmarkResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;

import java.util.List;

public interface BookmarkService {
    ApiResponse<Void> addBookmark(String nickname, AddBookmarkRequest request);

    ApiResponse<List<BookmarkResponse>> getBookmarks(String username);

    ApiResponse<Void> deleteBookmark(String username, Long destinationId,boolean isCountry);
}
