package com.footballfan.filter;

import com.footballfan.util.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService; // 필요 시 커스터마이징

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getBearerJwtFromRequest(request);
        if (jwtProvider.validateToken(token)){
            Claims claims = jwtProvider.verifyToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getId());
            setAuthentication(token, userDetails);
            // 인증 정보 설정
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken, UserDetails userDetails) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken, userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // HTTP 헤더에서 JWT 추출
    private String getBearerJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
