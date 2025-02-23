package com.business.i4_be.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponseWrapper {
    private final UserResponse user;

    public UserResponseWrapper(UserResponse user) {
        this.user = user;
    }
}
