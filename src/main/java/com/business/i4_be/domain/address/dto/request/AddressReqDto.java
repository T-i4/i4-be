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
    private String address;

    public Address toEntity(User user) {
        return Address.builder()
                .address(this.address)
                .user(user)
                .build();
    }
}
