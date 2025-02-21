package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.user.entity.User;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private final String status;
    private final String message;
    private final UserResponse user;

    public SignupResponse(String message, User user) {
        this.status = "success";
        this.message = message;
        this.user = new UserResponse(user);
    }
}