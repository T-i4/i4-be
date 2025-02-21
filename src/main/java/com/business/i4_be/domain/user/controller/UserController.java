package com.business.i4_be.domain.user.controller;

import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.request.UserUpdateWrapper;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.dto.response.UserResponseWrapper;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.domain.user.service.UserService;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseWrapper> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUser()));
    }

    // 특정 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseWrapper> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // 정보 수정
    @PutMapping("/update/me")
    public ResponseEntity<UserResponseWrapper> updateUser(
            @RequestBody UserUpdateWrapper requestWrapper,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (requestWrapper.getUser() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        return ResponseEntity.ok(userService.updateUser(userDetails.getUser().getId(), requestWrapper.getUser()));
    }

    // 주소 삭제
    @DeleteMapping("/delete/me/address")
    public ResponseEntity<UserResponseWrapper> deleteAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.deleteUserAddress(userDetails.getUser().getId()));
    }
}