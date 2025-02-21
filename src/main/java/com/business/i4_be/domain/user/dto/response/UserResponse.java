package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long userId;
    private final String username;
    private final String nickname;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String address;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().name();
        this.address = user.getAddress();
    }
}
