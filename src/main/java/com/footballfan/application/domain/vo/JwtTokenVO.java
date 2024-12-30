package com.footballfan.application.domain.vo;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class JwtTokenVO {
    private final String accessToken;
    private final String refreshToken;
    private final Long refreshTokenExpireTime;

    private final List<RoleType> roleType;
}
