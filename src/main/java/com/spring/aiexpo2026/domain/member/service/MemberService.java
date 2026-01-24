package com.spring.aiexpo2026.domain.member.service;

import com.spring.aiexpo2026.domain.member.data.request.SignInRequest;
import com.spring.aiexpo2026.domain.member.data.request.SignUpRequest;
import com.spring.aiexpo2026.domain.member.data.response.SignInResponse;
import com.spring.aiexpo2026.domain.member.data.response.SignUpResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

	ApiResponse<SignUpResponse> signUp(SignUpRequest request);

	ApiResponse<SignInResponse> signIn(SignInRequest request,
									   HttpServletResponse response);
}
