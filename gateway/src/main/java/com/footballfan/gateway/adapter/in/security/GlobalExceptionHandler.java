package com.footballfan.gateway.adapter.in.security;

import com.footballfan.gateway.adapter.in.web.response.ErrorCode;
import com.footballfan.gateway.filter.exception.TokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handTokenException(TokenException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.create(e, errorCode.getHttpStatus(), errorCode.getMessage());
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
