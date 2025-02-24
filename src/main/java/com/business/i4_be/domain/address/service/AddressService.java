package com.business.i4_be.domain.address.service;

import com.business.i4_be.domain.address.dto.request.AddressReqDto;
import com.business.i4_be.domain.address.dto.response.AddressResDto;
import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.address.repository.AddressRepository;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    // 내 주소 목록 조회
    public List<AddressResDto> getUserAddresses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user.getAddresses().stream()
                .map(AddressResDto::new)
                .collect(Collectors.toList());
    }

    // 주소 추가
    public AddressResDto addAddress(Long userId, AddressReqDto addressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Address newAddress = addressRequest.toEntity(user);
        addressRepository.save(newAddress);

        user.getAddresses().add(newAddress);
        userRepository.save(user);

        return new AddressResDto(newAddress);
    }

    // 주소 수정
    public AddressResDto updateAddress(Long userId, UUID addressId, String newAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!user.getAddresses().contains(address)) {
            throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        address.updateAddress(newAddress);
        addressRepository.save(address);

        return new AddressResDto(address);
    }

    // 주소 삭제
    public void deleteAddress(Long userId, UUID addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!user.getAddresses().contains(address)) {
            throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        user.getAddresses().remove(address);
        addressRepository.delete(address);
        userRepository.save(user);
    }
}
