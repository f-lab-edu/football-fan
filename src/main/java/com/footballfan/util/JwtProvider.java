package com.footballfan.util;

import com.footballfan.application.domain.vo.JwtTokenVO;
import com.footballfan.application.domain.vo.RoleType;
import com.footballfan.filter.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.footballfan.adapter.in.web.response.ErrorCode.INVALID_TOKEN;

@Component
public class JwtProvider {

    private final SecretKey signingKey;
    public static final Long ACCESS_TOKEN_EXPIRE_DURATION_SEC = 30 * 60L; //30분
    public static final Long REFRESH_TOKEN_EXPIRE_DURATION_SEC = 15 * 24 * 3500L; //15일

    public JwtProvider(@Value("${secret.key}") String secretKey) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public JwtTokenVO generateToken(List<RoleType> roles) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = generateToken(currentTimeMillis + ACCESS_TOKEN_EXPIRE_DURATION_SEC * 1000L);
        String refreshToken = generateToken(currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L);

        return new JwtTokenVO(accessToken, refreshToken, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L, roles);
    }

    public JwtTokenVO generateToken() {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = generateToken(currentTimeMillis + ACCESS_TOKEN_EXPIRE_DURATION_SEC * 1000L);
        String refreshToken = generateToken(currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L);

        return new JwtTokenVO(accessToken, refreshToken, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L, null);
    }

    public Claims verifyToken(String token) throws SignatureException, ExpiredJwtException {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) throws SignatureException, ExpiredJwtException {
        if (!StringUtils.hasText(token)) {
            throw new TokenException(INVALID_TOKEN);
        }
        return validateExpiration(verifyToken(token).getExpiration());
    }

    public boolean validateRefreshTokenAndExpiration(String refreshToken) throws SignatureException, ExpiredJwtException {
        Claims claims = verifyToken(refreshToken);
        return validateExpiration(claims.getExpiration());
    }

    private boolean validateExpiration(Date expiration) throws SignatureException, ExpiredJwtException {
        // 유효 true, 만료 false
        return expiration.after(new Date());
    }

    public Authentication getAuthentication(String token, UserDetails userDetails) {
        parseClaims(token);
        return new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
    }

    private void parseClaims(String token) {
        try {
            verifyToken(token);
        } catch (ExpiredJwtException e) {
            e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenException(INVALID_TOKEN);
//        } catch (SecurityException e) {
//            throw new TokenException(INVALID_JWT_SIGNATURE);
//        }
        }
    }

    private String generateToken(long expiration) {
        Date expirationDate = new Date(expiration);
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }
}