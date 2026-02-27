package com.spring.aiexpo2026.domain.auth.controller;

import com.spring.aiexpo2026.domain.auth.dto.request.*;
import com.spring.aiexpo2026.domain.auth.dto.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignUpResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignInResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignOutResponse;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.domain.auth.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final EmailService emailService;

	@PostMapping("/signup")
	public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		return memberService.signUp(signUpRequest);
	}

	@PostMapping("/signin")
	public ApiResponse<SignInResponse> signIn(@Valid @RequestBody SignInRequest request,
											  HttpServletResponse response) {
		return memberService.signIn(request, response);
	}

	@PostMapping("/signout")
	public ApiResponse<SignOutResponse> signOut(HttpServletRequest httpServletRequest,
												HttpServletResponse httpServletResponse) {
		return memberService.signOut(httpServletRequest, httpServletResponse);
	}

	@PostMapping("/email/send")
	public void sendEmail(@Valid @RequestBody SendEmailRequest request) {
		emailService.sendEmail(request);
	}

	@PostMapping("/email/verify")
	public boolean verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
		return emailService.verifyEmail(request);
	}

	@PostMapping("/signup/email/verify")
	public void verifyEmailForSignUp(@Valid @RequestBody VerifyEmailRequest verifyEmailRequest) {
		emailService.verifyEmailForSignUp(verifyEmailRequest);
	}

	@PutMapping("/password")
	public ApiResponse<ChangePasswordResponse> changePassword(HttpServletRequest httpServletRequest,
															  @Valid @RequestBody ChangePasswordRequest request) {
		return memberService.changePassword(httpServletRequest, request);
	}

	@PostMapping("/password/reset")
	public boolean resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
		return memberService.resetPassword(resetPasswordRequest);
	}
}
