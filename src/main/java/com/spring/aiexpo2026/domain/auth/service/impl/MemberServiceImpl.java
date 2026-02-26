package com.spring.aiexpo2026.domain.auth.service.impl;

import com.spring.aiexpo2026.domain.auth.dto.request.*;
import com.spring.aiexpo2026.domain.auth.dto.response.SignOutResponse;
import com.spring.aiexpo2026.domain.auth.entity.Role;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.domain.auth.service.TokenService;
import com.spring.aiexpo2026.domain.auth.dto.response.ChangePasswordResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignInResponse;
import com.spring.aiexpo2026.domain.auth.dto.response.SignUpResponse;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.auth.service.MemberService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	private final EmailService emailService;
	private final TokenService tokenService;

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	@Transactional
	public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
		if (memberRepository.existsByNickname(signUpRequest.nickname())) {
			throw new ApplicationException(AuthStatusCode.USERNAME_ALREADY_EXIST);
		}
		if (memberRepository.existsByEmail(signUpRequest.email())) {
			throw new ApplicationException(AuthStatusCode.EMAIL_ALREADY_EXIST);
		}
		emailService.verifyEmail(new VerifyEmailRequest(
				signUpRequest.email(),
				signUpRequest.authNum())
		);

		String encodedPassword = passwordEncoder.encode(signUpRequest.passwordHash());
		Member member = signUpRequest.toEntity(encodedPassword);

		memberRepository.save(member);

		return ApiResponse.ok(SignUpResponse.of("가입되었습니다."));
	}

	@Override
	public ApiResponse<SignInResponse> signIn(SignInRequest request,
											  HttpServletResponse response) {
		Member member = memberRepository.findByNickname(request.nickname()).orElseThrow(() ->
				new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS));

		if (!passwordEncoder.matches(request.passwordHash(), member.getPasswordHash())) {
			throw new ApplicationException(AuthStatusCode.INVALID_CREDENTIALS);
		}

		if (member.getRole() == Role.USER_NOT_VERIFIED) {
			throw new ApplicationException(AuthStatusCode.USER_NOT_VERIFY);
		}

		GenerateTokenRequest generateTokenRequest = new GenerateTokenRequest (
				member.getNickname(),
				member.getRole()
		);

		String accessToken = tokenService.generateAccessToken(generateTokenRequest, response);

		return ApiResponse.ok(SignInResponse.of(accessToken));
	}

	@Override
	public ApiResponse<SignOutResponse> signOut(HttpServletRequest httpServletRequest,
												HttpServletResponse httpServletResponse) {
		getMemberFromToken(httpServletRequest);
		tokenService.deleteAccessToken(httpServletRequest, httpServletResponse);

		return ApiResponse.ok(SignOutResponse.of("로그아웃 되었습니다."));
	}

	@Override
	@Transactional
	public ApiResponse<ChangePasswordResponse> changePassword(HttpServletRequest httpServletRequest,
															  ChangePasswordRequest request) {
		Member member = getMemberFromToken(httpServletRequest);

		member.changePassword(
				request,
				passwordEncoder
		);

		return ApiResponse.ok(ChangePasswordResponse.of("변경되었습니다."));
	}

	@Override
	@Transactional
	public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String code = valueOperations.get(resetPasswordRequest.email());

		Member member = memberRepository.findByEmail(resetPasswordRequest.email()).orElseThrow(()
				-> new ApplicationException(AuthStatusCode.CANNOT_FIND_EMAIL));

		if (Objects.equals(code, resetPasswordRequest.authNum())) {
			member.resetPassword(passwordEncoder.encode(resetPasswordRequest.passwordHash()));
			redisTemplate.delete(resetPasswordRequest.email());
			return true;
		} else {
			throw new ApplicationException(AuthStatusCode.CANNOT_VERIFY_EMAIL);
		}
	}

	public Member getMemberFromToken(HttpServletRequest httpServletRequest) {
		return tokenService.getMemberFromAccessToken(httpServletRequest);
	}
}