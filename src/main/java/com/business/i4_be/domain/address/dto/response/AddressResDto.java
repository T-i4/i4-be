package com.business.i4_be.domain.address.dto.response;

import com.business.i4_be.domain.address.entity.Address;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddressResDto {
    private final UUID addressId;
    private final String address;

    public AddressResDto(Address address) {
        this.addressId = address.getAddressId();
        this.address = address.getAddress();
    }
}
