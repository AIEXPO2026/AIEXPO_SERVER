package com.spring.aiexpo2026.domain.bookmark.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookmarkRequest {
    private Long destinationId;
    private Long countryId;
}
