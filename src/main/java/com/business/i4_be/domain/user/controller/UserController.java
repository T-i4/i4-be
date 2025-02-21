package com.business.i4_be.domain.user.controller;

import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.request.UserUpdateWrapper;
import com.business.i4_be.domain.user.dto.response.UserResponse;
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
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse userResponse = userService.getMyInfo(userDetails.getUser());
        return ResponseEntity.ok(userResponse);
    }

    // 모든 유저 조회
    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // 특정 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 정보 수정
    @PutMapping("/update/me")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UserUpdateWrapper requestWrapper,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // user 필드가 없으면 예외 처리
        if (requestWrapper.getUser() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        UpdateUserRequest request = requestWrapper.getUser(); // User 객체 추출
        return ResponseEntity.ok(userService.updateUser(userDetails.getUser().getId(), request));
    }

    // 주소 삭제
    @DeleteMapping("/delete/me/address")
    public ResponseEntity<UserResponse> deleteAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse userResponse = userService.deleteUserAddress(userDetails.getUser().getId());
        return ResponseEntity.ok(userResponse);
    }

}
