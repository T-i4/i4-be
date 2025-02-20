package com.business.i4_be.domain.user.dto.response;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private String message;
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String token;
}