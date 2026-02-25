package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.dto.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.VerifyEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.response.VerifyEmailResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;

public interface EmailService {

	/**
	 * 이메일 인증 관련
	 */
	void sendEmail(SendEmailRequest request);

	ApiResponse<VerifyEmailResponse> verifyEmail(VerifyEmailRequest request);
}
