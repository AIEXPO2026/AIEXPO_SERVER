package com.spring.aiexpo2026.domain.auth.service;

import com.spring.aiexpo2026.domain.auth.data.request.GenerateTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {

	String generateAccessToken(GenerateTokenRequest request,
							   HttpServletResponse response);

	void deleteAccessToken(HttpServletRequest request,
						   HttpServletResponse response);
}
