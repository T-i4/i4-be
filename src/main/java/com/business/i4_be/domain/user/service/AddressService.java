package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.user.dto.request.AddressRequest;
import com.business.i4_be.domain.user.dto.response.AddressResponse;
import com.business.i4_be.domain.user.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.AddressRepository;
import com.business.i4_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponse createAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Address newAddress = request.toEntity(user);
        addressRepository.save(newAddress);

        return AddressResponse.fromEntity(newAddress);
    }

    public AddressResponse updateAddress(UUID addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

        address.updateAddress(request.getAddress());
        addressRepository.save(address);

        return AddressResponse.fromEntity(address);
    }

    public void deleteAddress(UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

        addressRepository.delete(address);
    }
}
