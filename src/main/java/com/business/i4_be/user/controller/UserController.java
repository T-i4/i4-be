package com.business.i4_be.user.controller;

import com.business.i4_be.user.dto.request.SignupRequest;
import com.business.i4_be.user.dto.response.SignupResponse;
import com.business.i4_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // 기본 URL 설정
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")  // 회원가입 API
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = userService.signup(request);
        return ResponseEntity.ok(response);
    }
}
