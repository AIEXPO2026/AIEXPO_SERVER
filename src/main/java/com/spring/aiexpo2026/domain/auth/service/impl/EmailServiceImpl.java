package com.spring.aiexpo2026.domain.auth.service.impl;

import com.spring.aiexpo2026.domain.auth.dto.request.SendEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.request.VerifyEmailRequest;
import com.spring.aiexpo2026.domain.auth.dto.response.VerifyEmailResponse;
import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.entity.Role;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.auth.service.EmailService;
import com.spring.aiexpo2026.global.config.RedisConfig;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final RedisTemplate<String, String> redisTemplate;
	private final MemberRepository memberRepository;

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
        <!DOCTYPE html>
        <html lang="ko">
        <body style="margin:0; padding:0; background-color:#f4f6f8; font-family: Arial, Helvetica, sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
              <td align="center" style="padding:40px 16px;">
                <table width="480" cellpadding="0" cellspacing="0"
                       style="background-color:#ffffff; border-radius:12px; padding:32px;
                              box-shadow:0 4px 12px rgba(0,0,0,0.08);">
                  
                  <tr>
                    <td style="text-align:center; padding-bottom:24px;">
                      <h2 style="margin:0; color:#222;"> 회원가입 인증 코드</h2>
                    </td>
                  </tr>

                  <tr>
                    <td style="color:#555; font-size:14px; line-height:1.6;">
                      안녕하세요.<br/>
                      회원가입을 완료하려면 아래 인증 코드를 입력해주세요.
                    </td>
                  </tr>

                  <tr>
                    <td align="center" style="padding:24px 0;">
                      <div style="
                        display:inline-block;
                        padding:16px 32px;
                        font-size:24px;
                        font-weight:bold;
                        letter-spacing:4px;
                        color:#1a73e8;
                        background-color:#eef4ff;
                        border-radius:8px;">
                        {{AUTH_CODE}}
                      </div>
                    </td>
                  </tr>

                  <tr>
                    <td style="color:#777; font-size:13px; line-height:1.6;">
                      인증 코드는 <b>3분간</b> 유효합니다.<br/>
                      본인이 요청하지 않은 경우 이 메일을 무시해주세요.
                    </td>
                  </tr>

                  <tr>
                    <td style="padding-top:32px; text-align:center; font-size:12px; color:#aaa;">
                      اللّٰهُ أَكْبَر
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
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
		// Redis에 3분간 저장
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(request.email(), Integer.toString(authNum), 3, TimeUnit.MINUTES);
	}

	@Override
	@Transactional
	public ApiResponse<VerifyEmailResponse> verifyEmail(VerifyEmailRequest request) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String code = valueOperations.get(request.email());

		Member member = memberRepository.findByEmail(request.email()).orElseThrow(()
				-> new ApplicationException(AuthStatusCode.CANNOT_FIND_EMAIL));

		if (Objects.equals(code, request.authNum())) {
			member.updateRole(Role.USER);
			redisTemplate.delete(request.email());
			return ApiResponse.ok(VerifyEmailResponse.success("이메일이 인증되었습니다."));
		} else {
			throw new ApplicationException(AuthStatusCode.CANNOT_VERIFY_EMAIL);
		}
	}
}
