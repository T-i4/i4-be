package com.business.i4_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// PUT 요청 시 user 필드
@Getter
@NoArgsConstructor
public class UserUpdateWrapper {
    private UpdateUserRequest user;
}
