package com.spring.aiexpo2026.domain.credit.data.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditResponse {
    private int credit;

    public static CreditResponse from(int credit) {
        return new CreditResponse(credit);
    }
}
