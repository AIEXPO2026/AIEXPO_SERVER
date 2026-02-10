package com.spring.aiexpo2026.global.jwt;

import com.spring.aiexpo2026.domain.auth.data.request.GenerateTokenRequest;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import com.spring.aiexpo2026.global.exception.statuscode.CommonStatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

	private final SecretKey key;
	private final long tokenValidity = 7200000; // 2시간

	public JwtProvider() {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.load();

		String secret = dotenv.get("JWT_SECRET");

		if (secret == null || secret.isEmpty()) {
			throw new ApplicationException(CommonStatusCode.UNKNOWN_JWT_SECRET);
		}

		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// 엑세스 토큰
	public String generateAccessToken(GenerateTokenRequest request) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + tokenValidity);

		return Jwts
				.builder()
				.subject(request.nickname())

				.claim("tokenType", "accessToken")
				.claim("role", request.role())

				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}

	public String getNickname(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}

	public String getEmail(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.get("email", String.class);
	}

	public String getRole(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.get("role").toString();
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> jwsClaims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);

			Claims claims = jwsClaims.getPayload();
			Date exp = claims.getExpiration();

			if (exp.before(new Date())) {
				return false;
			}

			String tokenType = claims.get("tokenType").toString();
			if (tokenType.equalsIgnoreCase("refreshToken") || tokenType.equalsIgnoreCase("accessToken")) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
