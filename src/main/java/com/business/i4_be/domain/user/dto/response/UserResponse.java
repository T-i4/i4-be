package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.address.dto.response.AddressResDto;
import com.business.i4_be.domain.user.entity.User;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponse {
    private final Long userId;
    private final String username;
    private final String nickname;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final List<AddressResDto> addresses;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().name();

        this.addresses = user.getAddresses() != null
                ? user.getAddresses().stream().map(AddressResDto::new).collect(Collectors.toList())
                : Collections.emptyList();
    }
}