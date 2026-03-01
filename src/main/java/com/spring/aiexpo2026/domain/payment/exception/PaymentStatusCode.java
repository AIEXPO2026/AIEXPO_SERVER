package com.spring.aiexpo2026.domain.payment.exception;

import com.spring.aiexpo2026.global.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentStatusCode implements StatusCode {

    PAYMENT_CONFIRM_FAILED("PAYMENT_CONFIRM_FAILED", "결제 승인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_AMOUNT_MISMATCH("PAYMENT_AMOUNT_MISMATCH", "결제 금액이 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
