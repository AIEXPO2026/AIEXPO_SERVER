package com.spring.aiexpo2026.domain.profile.controller;

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

    @GetMapping("/bookmark")
    public
    ApiResponse<List<BookmarkResponse>> getBookmarks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return bookmarkService.getBookmarks(userDetails.getUsername());
    }

    @DeleteMapping("/bookmark/{destinationId}")
    public ApiResponse<Void> deleteBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long destinationId
    ) {
        return bookmarkService.deleteBookmark(userDetails.getUsername(), destinationId);
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
