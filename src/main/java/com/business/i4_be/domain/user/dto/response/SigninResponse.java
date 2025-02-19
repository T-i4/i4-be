package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
}
