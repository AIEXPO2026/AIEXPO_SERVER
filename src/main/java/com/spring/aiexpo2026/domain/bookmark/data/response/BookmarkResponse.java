package com.spring.aiexpo2026.domain.bookmark.data.response;

import com.spring.aiexpo2026.domain.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookmarkResponse {
    private Long bookmarkId;
    private Long destinationId;
    private String name;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal baseScore;

    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getDestination().getId(),
                bookmark.getDestination().getName(),
                bookmark.getDestination().getCity(),
                bookmark.getDestination().getLatitude(),
                bookmark.getDestination().getLongitude(),
                bookmark.getDestination().getBaseScore()
        );
    }
}
