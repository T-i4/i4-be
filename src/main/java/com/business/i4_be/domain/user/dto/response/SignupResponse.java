package com.business.i4_be.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private final Long id;

    public SignupResponse(Long id) {
        this.id = id;
    }
}
