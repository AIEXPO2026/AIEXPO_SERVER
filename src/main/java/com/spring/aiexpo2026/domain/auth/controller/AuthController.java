package com.spring.aiexpo2026.domain.auth.controller;

import com.spring.aiexpo2026.domain.auth.data.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.data.request.VerifyEmailRequest;
import com.spring.aiexpo2026.domain.auth.data.response.VerifyEmailResponse;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.domain.member.data.request.ChangePasswordRequest;
import com.spring.aiexpo2026.domain.member.data.request.SignInRequest;
import com.spring.aiexpo2026.domain.member.data.request.SignUpRequest;
import com.spring.aiexpo2026.domain.member.data.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.member.data.response.SignInResponse;
import com.spring.aiexpo2026.domain.member.data.response.SignUpResponse;
import com.spring.aiexpo2026.domain.member.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
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
	public ApiResponse<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
		return memberService.changePassword(request);
	}
}
