package com.business.i4_be.domain.user.dto.response;

import com.business.i4_be.domain.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private UUID addressId;
    private String address;

    public static AddressResponse fromEntity(Address address) {
        return new AddressResponse(address.getAddressId(), address.getAddress());
    }
}