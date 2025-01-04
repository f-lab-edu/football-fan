package com.footballfan.gateway.adapter.in.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footballfan.gateway.adapter.in.web.response.ErrorCode;
import com.footballfan.gateway.filter.exception.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException instanceof TokenException tokenException) {
            ErrorCode errorCode = tokenException.getErrorCode();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", errorCode.getHttpStatus().value());
            errorResponse.put("message", tokenException.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else {
            // 기본 인증 실패 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.put("message", "Unauthorized");

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
