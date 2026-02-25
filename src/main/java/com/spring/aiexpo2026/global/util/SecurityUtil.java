package com.spring.aiexpo2026.global.util;

import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.exception.MemberStatusCode;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final MemberRepository memberRepository;

    public Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ApplicationException(MemberStatusCode.CANNOT_FIND_MEMBER));
    }
}
