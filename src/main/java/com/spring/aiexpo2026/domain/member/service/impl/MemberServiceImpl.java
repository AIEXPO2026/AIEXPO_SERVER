package com.spring.aiexpo2026.domain.member.service.impl;

import com.spring.aiexpo2026.domain.auth.data.request.GenerateTokenRequest;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.service.TokenService;
import com.spring.aiexpo2026.domain.member.data.request.ChangePasswordRequest;
import com.spring.aiexpo2026.domain.member.data.request.SignInRequest;
import com.spring.aiexpo2026.domain.member.data.request.SignUpRequest;
import com.spring.aiexpo2026.domain.member.data.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.member.data.response.SignInResponse;
import com.spring.aiexpo2026.domain.member.data.response.SignUpResponse;
import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.exception.MemberStatusCode;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
import com.spring.aiexpo2026.domain.member.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	@Override
	@Transactional
	public ApiResponse<SignUpResponse> signUp(SignUpRequest request) {
		if (memberRepository.existsByNickname(request.nickname())) {
			throw new ApplicationException(AuthStatusCode.USERNAME_ALREADY_EXIST);
		}
		if (memberRepository.existsByEmail(request.email())) {
			throw new ApplicationException(AuthStatusCode.EMAIL_ALREADY_EXIST);
		}

		String encodedPassword = passwordEncoder.encode(request.passwordHash());
		Member member = request.toEntity(encodedPassword);

		memberRepository.save(member);

		return ApiResponse.ok(SignUpResponse.success());
	}

	@Override
	public ApiResponse<SignInResponse> signIn(SignInRequest request,
											  HttpServletResponse response) {
		Member member = memberRepository.findByNickname(request.nickname()).orElseThrow(() ->
				new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS));

		if (!passwordEncoder.matches(request.passwordHash(), member.getPasswordHash())) {
			throw new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS);
		}

		GenerateTokenRequest generateTokenRequest = new GenerateTokenRequest (
				member.getNickname(),
				member.getRole()
		);

		String accessToken = tokenService.generateAccessToken(generateTokenRequest, response);

		return ApiResponse.ok(SignInResponse.success(accessToken));
	}

	@Override
	@Transactional
	public ApiResponse<ChangePasswordResponse> changePassword(ChangePasswordRequest request) {
		Member member = memberRepository.findByNickname(request.nickname()).orElseThrow(()
				-> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));

		if (!passwordEncoder.matches(request.oldPassword(), member.getPasswordHash())) {
			throw new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS);
		}

		member.updatePassword(passwordEncoder.encode(request.newPassword()));

		return ApiResponse.ok(ChangePasswordResponse.success());
	}
}
