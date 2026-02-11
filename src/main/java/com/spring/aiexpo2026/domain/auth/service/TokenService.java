package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.dto.request.GenerateTokenRequest;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {

	/**
	 * 토큰 발급 관련
	 */
	String generateAccessToken(GenerateTokenRequest request,
							   HttpServletResponse response);

	void deleteAccessToken(HttpServletRequest request,
						   HttpServletResponse response);

	Member getMemberFromAccessToken(HttpServletRequest request);
}
