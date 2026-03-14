package com.spring.aiexpo2026.domain.profile.controller;

import com.spring.aiexpo2026.domain.bookmark.data.request.AddBookmarkRequest;
import com.spring.aiexpo2026.domain.bookmark.data.response.BookmarkResponse;
import com.spring.aiexpo2026.domain.bookmark.service.BookmarkService;
import com.spring.aiexpo2026.domain.credit.data.request.ChargeCreditRequest;
import com.spring.aiexpo2026.domain.credit.data.response.CreditResponse;
import com.spring.aiexpo2026.domain.credit.service.CreditService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final BookmarkService bookmarkService;
    private final CreditService creditService;

    @PostMapping("/bookmark")
    public ApiResponse<Void> addBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddBookmarkRequest request
    ) {
        return bookmarkService.addBookmark(userDetails.getUsername(), request);
    }

    @GetMapping("/bookmark")
    public
    ApiResponse<List<BookmarkResponse>> getBookmarks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return bookmarkService.getBookmarks(userDetails.getUsername());
    }

    @DeleteMapping("/bookmark/{id}")
    public ApiResponse<Void> deleteBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean isCountry
    ) {
        return bookmarkService.deleteBookmark(userDetails.getUsername(), id, isCountry);
    }

    @GetMapping("/credit")
    public ApiResponse<CreditResponse> getCredit(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return creditService.getCredit(userDetails.getUsername());
    }

    @PostMapping("/credit")
    public ApiResponse<CreditResponse> chargeCredit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChargeCreditRequest request
    ) {
        return creditService.chargeCredit(userDetails.getUsername(), request);
    }
}
