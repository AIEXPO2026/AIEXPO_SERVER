package com.spring.aiexpo2026.domain.auth.service.impl;

import com.spring.aiexpo2026.domain.auth.dto.request.GenerateTokenRequest;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.service.TokenService;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;
	private final MemberRepository memberRepository;

	// 토큰 발급(로그인)
	@Override
	public String generateAccessToken(GenerateTokenRequest request,
									  HttpServletResponse response) {
		String accessToken = jwtProvider.generateAccessToken(request);

		redisTemplate.opsForValue().set("accessToken:" + request.nickname(), accessToken, 2, TimeUnit.HOURS);

		Cookie accessCookie = new Cookie("accessToken", accessToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(true);
		accessCookie.setMaxAge(60 * 60 * 2); // 2시간
		response.addCookie(accessCookie);
		return accessToken;
	}

	// 토큰 삭제(로그아웃)
	@Override
	public void deleteAccessToken(HttpServletRequest request,
								  HttpServletResponse response) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		String username = getMemberFromAccessToken(request).getNickname();

		String savedAccessToken = valueOperations.get("accessToken:" + username);

		if (savedAccessToken == null) {
			throw new ApplicationException(AuthStatusCode.ALREADY_LOGGED_OUT);
		} else {
			// 엑세스 토큰 만료(쿠키)
			Cookie accessCookie = new Cookie("accessToken", null);
			accessCookie.setPath("/");
			accessCookie.setHttpOnly(false);
			accessCookie.setMaxAge(0); // 즉시 만료
			response.addCookie(accessCookie);

			redisTemplate.delete("accessToken:" + username);
		}
	}

	@Override
	public Member getMemberFromAccessToken(HttpServletRequest request) {
		// Authorization 헤더 우선, 없으면 쿠키에서 조회
		String accessToken = jwtProvider.resolveToken(request);
		if (accessToken == null) {
			accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
					.filter(cookie -> "accessToken".equals(cookie.getName()))
					.map(Cookie::getValue).findFirst().orElseThrow(()
							-> new ApplicationException(AuthStatusCode.INVALID_TOKEN));
		}

		if (!jwtProvider.validateToken(accessToken)) {
			throw new ApplicationException(AuthStatusCode.INVALID_TOKEN);
		}
		String nickname = jwtProvider.getNickname(accessToken);

		return memberRepository.findByNickname(nickname).orElseThrow(()
				-> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));
	}
}
