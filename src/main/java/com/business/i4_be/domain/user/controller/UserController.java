package com.business.i4_be.domain.user.controller;

import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.domain.user.service.UserService;
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
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
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
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.updateUser(userDetails.getUser().getId(), request));
    }
}
