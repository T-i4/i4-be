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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseWrapper> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUsername()));
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

    // 회원 탈퇴
    @DeleteMapping("/delete/me")
    public ResponseEntity<Map<String, String>> deleteMyAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.deleteMyAccount(userDetails.getUser().getId()));
    }

    // 회원 삭제
    // MASTER 는 모든 회원을 삭제 가능
    // ADMIN 은 OWNER 와 USER 만 삭제 가능
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userId, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}