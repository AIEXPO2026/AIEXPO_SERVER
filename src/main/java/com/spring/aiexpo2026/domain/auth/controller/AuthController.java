package com.spring.aiexpo2026.domain.auth.controller;

import com.spring.aiexpo2026.domain.member.data.request.SignUpRequest;
import com.spring.aiexpo2026.domain.member.data.response.SignUpResponse;
import com.spring.aiexpo2026.domain.member.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ApiResponse<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
		return memberService.signUp(request);
	}
}
