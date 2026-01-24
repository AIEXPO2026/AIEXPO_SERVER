package com.spring.aiexpo2026.domain.member.service;

import com.spring.aiexpo2026.domain.member.data.request.SignUpRequest;
import com.spring.aiexpo2026.domain.member.data.response.SignUpResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;

public interface MemberService {

	ApiResponse<SignUpResponse> signUp(SignUpRequest request);
}
