package com.business.i4_be.domain.address.controller;

import com.business.i4_be.domain.address.dto.request.AddressReqDto;
import com.business.i4_be.domain.address.dto.response.AddressResDto;
import com.business.i4_be.domain.address.service.AddressService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 주소 추가
    @PostMapping("")
    public ResponseEntity<AddressResDto> addAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody AddressReqDto request) {
        return ResponseEntity.ok(addressService.addAddress(userDetails.getUser().getId(), request));
    }

    // 내 주소 조회
    @GetMapping("/me")
    public ResponseEntity<List<AddressResDto>> getUserAddresses(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(addressService.getUserAddresses(userDetails.getUser().getId()));
    }

    // 주소 수정
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResDto> updateAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID addressId,
            @RequestBody AddressReqDto request) {
        return ResponseEntity.ok(addressService.updateAddress(userDetails.getUser().getId(), addressId, request.getAddress()));
    }

    // 주소 삭제
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID addressId
    ) {
        addressService.deleteAddress(userDetails.getUser().getId(), addressId);
        return ResponseEntity.noContent().build();
    }
}