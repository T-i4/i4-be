package com.business.i4_be.domain.user.dto.request;

import com.business.i4_be.domain.address.dto.request.AddressReqDto;
import com.business.i4_be.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String password;
    private String role;

    private List<AddressReqDto> addresses;

    public List<AddressReqDto> getAddresses() {
        return addresses;
    }
}
