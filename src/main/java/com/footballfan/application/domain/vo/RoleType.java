package com.footballfan.application.domain.vo;

import lombok.Getter;

@Getter
public enum RoleType {
    ALL("All User asscess"),
    ADMIN("Administrator with full access"),
    USER("Regular user with limited access"),
    DEVELOPER("Developer with access to development tools"),
    OIDC_USER("User authenticated via OpenID Connect");

    private final String desc;

    RoleType(String desc) {
        this.desc = desc;
    }

}
