package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.dto.request.ChangePasswordRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.SignInRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.SignUpRequest;
import com.spring.aiexpo2026.domain.auth.dto.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignInResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignOutResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignUpResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

	ApiResponse<SignUpResponse> signUp(SignUpRequest request);

	ApiResponse<SignInResponse> signIn(SignInRequest request,
									   HttpServletResponse response);

	ApiResponse<ChangePasswordResponse> changePassword(HttpServletRequest httpServletRequest,
													   ChangePasswordRequest request);

	ApiResponse<SignOutResponse> signOut(HttpServletRequest request,
										 HttpServletResponse response);
}
