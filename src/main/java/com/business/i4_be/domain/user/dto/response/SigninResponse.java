package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.jwt.TokenDto;
import lombok.Getter;

@Getter
public class SigninResponse {
    private final String status;
    private final String message;
    private final String accessToken;
    private final String refreshToken;
    private final UserResponse user;

    public SigninResponse(String message, TokenDto tokenDto, User user) {
        this.status = "success";
        this.message = message;
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.user = new UserResponse(user);
    }
}