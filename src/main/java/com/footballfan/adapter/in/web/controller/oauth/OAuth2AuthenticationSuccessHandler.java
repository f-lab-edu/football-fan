package com.footballfan.adapter.in.web.controller.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footballfan.application.domain.vo.JwtTokenVO;
import com.footballfan.application.domain.vo.RoleType;
import com.footballfan.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtUtil;
    private static final String REDIRECT_SUCCESS_URL = "/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // 제공자에 따라 키 이름 다를 수 있음

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found from OAuth2 provider");
            return;
        }

        HashMap<Object, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("token", jwtUtil.generateToken());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

        response.sendRedirect(REDIRECT_SUCCESS_URL);
    }
}
