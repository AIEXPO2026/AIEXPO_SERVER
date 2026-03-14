package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.dto.request.*;
import com.spring.aiexpo2026.domain.auth.dto.response.*;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

	ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest);

	ApiResponse<SignInResponse> signIn(SignInRequest request,
									   HttpServletResponse response);

	ApiResponse<ChangeNicknameResponse> changeNickname(HttpServletRequest httpServletRequest,
													   HttpServletResponse httpServletResponse,
													   ChangeNicknameRequest changeNicknameRequest);

	ApiResponse<ChangePasswordResponse> changePassword(HttpServletRequest httpServletRequest,
													   ChangePasswordRequest request);

	boolean resetPassword(ResetPasswordRequest resetPasswordRequest);

	ApiResponse<SignOutResponse> signOut(HttpServletRequest request,
										 HttpServletResponse response);

	ApiResponse<DeleteMemberResponse> deleteMember(HttpServletRequest httpServletRequest,
												   HttpServletResponse httpServletResponse);
}
