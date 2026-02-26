package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.dto.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.VerifyEmailRequest;

public interface EmailService {

	/**
	 * 이메일 인증 관련
	 */
	void sendEmail(SendEmailRequest request);

	boolean verifyEmail(VerifyEmailRequest verifyEmailRequest);

	void verifyEmailForSignUp(VerifyEmailRequest verifyEmailRequest);
}
