package com.spring.aiexpo2026.domain.credit.service;

import com.spring.aiexpo2026.domain.credit.data.request.ChargeCreditRequest;
import com.spring.aiexpo2026.domain.credit.data.response.CreditResponse;
import com.spring.aiexpo2026.global.data.ApiResponse;

public interface CreditService {
    ApiResponse<CreditResponse> getCredit(String username);

    ApiResponse<CreditResponse> chargeCredit(String username, ChargeCreditRequest request);
}
