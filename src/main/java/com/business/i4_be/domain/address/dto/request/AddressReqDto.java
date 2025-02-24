package com.business.i4_be.domain.address.dto.request;

import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressReqDto {
    private AddressUser user;  // 클라이언트에서 넘어온 user 객체

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressUser {
        private String address;
    }

    public Address toEntity(User user) {
        return Address.builder()
                .address(this.user.getAddress())  // 내부 user 객체에서 address 값 추출
                .user(user)
                .build();
    }
}
