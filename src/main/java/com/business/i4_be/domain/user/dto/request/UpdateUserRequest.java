package com.business.i4_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String password;
}
