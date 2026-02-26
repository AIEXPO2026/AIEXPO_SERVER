package com.spring.aiexpo2026.domain.credit.service.impl;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import com.spring.aiexpo2026.domain.auth.exception.AuthStatusCode;
import com.spring.aiexpo2026.domain.auth.repository.MemberRepository;
import com.spring.aiexpo2026.domain.credit.data.request.ChargeCreditRequest;
import com.spring.aiexpo2026.domain.credit.data.response.CreditResponse;
import com.spring.aiexpo2026.domain.credit.service.CreditService;
import com.spring.aiexpo2026.global.data.ApiResponse;
import com.spring.aiexpo2026.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<CreditResponse> getCredit(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        return ApiResponse.ok(CreditResponse.from(member.getCredit()));
    }

    @Override
    @Transactional
    public ApiResponse<CreditResponse> chargeCredit(String nickname, ChargeCreditRequest request) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ApplicationException(AuthStatusCode.CANNOT_FIND_MEMBER));

        memberRepository.chargeCredit(member.getId(), request.getCredit());

        return ApiResponse.ok(CreditResponse.from(member.getCredit() + request.getCredit()));
    }
}
