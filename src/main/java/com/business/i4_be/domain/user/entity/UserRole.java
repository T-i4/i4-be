package com.business.i4_be.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN"),
    MASTER("ROLE_MASTER");

    private final String authority;
}