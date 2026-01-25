package com.spring.aiexpo2026.domain.auth.service.impl;

import com.spring.aiexpo2026.domain.auth.data.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.data.request.VerifyEmailRequest;
import com.spring.aiexpo2026.domain.auth.data.response.VerifyEmailResponse;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.global.config.RedisConfig;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final RedisConfig redisConfig;

	@Value("${spring.mail.username}")
	private String serviceName;

	// 인증번호
	public int authNum() {
		return 100000 + new Random().nextInt(899999);
	}

	@Override
	public void sendEmail(SendEmailRequest request) {
		int authNum = authNum();

		String title = "서비스 이름";
		String message = """
				<p>'서비스 이름' 회원가입용 인증 코드입니다.</p>
				            <p>인증코드는 아래와 같습니다.</p>
				            <p><b>{{AUTH_CODE}}</b></p>
				            <p>이 코드를 입력하여 인증을 완료해주세요. 본 인증코드는 3분간 유효합니다.</p>
			""";

		String content = message.replace("{{AUTH_CODE}}", String.valueOf(authNum));
		MimeMessage sendMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(sendMessage, true, "utf-8");
			helper.setFrom(serviceName, "서비스 이름");
			helper.setTo(request.email());
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(sendMessage);
		} catch (Exception e) {
			throw new ApplicationException(AuthStatusCode.CANNOT_VERIFY_EMAIL);
		}
		// Redis에 5분간 저장
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		valueOperations.set(request.email(), Integer.toString(authNum), 3, TimeUnit.MINUTES);
	}

	@Override
	public ApiResponse<VerifyEmailResponse> verifyEmail(VerifyEmailRequest request) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String code = valueOperations.get(request.email());

		if (Objects.equals(code, request.authNum())) {
			redisConfig.redisTemplate().delete(request.email());
			redisConfig.redisTemplate().delete(String.valueOf(request.authNum()));
			return ApiResponse.ok(VerifyEmailResponse.success());
		} else {
			throw new ApplicationException(AuthStatusCode.CANNOT_VERIFY_EMAIL);
		}
	}
}
