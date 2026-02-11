package com.spring.aiexpo2026.domain.auth.controller;

import com.spring.aiexpo2026.domain.auth.dto.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.VerifyEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.response.VerifyEmailResponse;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.domain.auth.dto.request.ChangePasswordRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.SignInRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.SignUpRequest;
import com.spring.aiexpo2026.domain.auth.dto.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignInResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignUpResponse;
import com.spring.aiexpo2026.domain.auth.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final EmailService emailService;

	@PostMapping("/signup")
	public ApiResponse<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
		return memberService.signUp(request);
	}

	@PostMapping("/signin")
	public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request,
											  HttpServletResponse response) {
		return memberService.signIn(request, response);
	}

	@PostMapping("/email/send")
	public void sendEmail(@RequestBody SendEmailRequest request) {
		emailService.sendEmail(request);
	}

	@PostMapping("/email/verify")
	public ApiResponse<VerifyEmailResponse> verifyEmail(@RequestBody VerifyEmailRequest request) {
		return emailService.verifyEmail(request);
	}

	@PutMapping("/password")
	public ApiResponse<ChangePasswordResponse> changePassword(HttpServletRequest httpServletRequest,
															  @RequestBody ChangePasswordRequest request) {
		return memberService.changePassword(httpServletRequest, request);
	}
}
