package com.footballfan.adapter.in.web.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 필요한 다른 에러 코드 추가
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired token"),
    TOKEN_MISSING(HttpStatus.BAD_REQUEST, "Token is missing"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}