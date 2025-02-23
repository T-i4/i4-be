package com.business.i4_be.domain.user.controller;

import com.business.i4_be.domain.user.dto.request.SigninRequest;
import com.business.i4_be.domain.user.dto.request.SignupRequest;
import com.business.i4_be.domain.user.dto.request.UserSigninRequest;
import com.business.i4_be.domain.user.dto.request.UserSignupRequest;
import com.business.i4_be.domain.user.dto.response.SigninResponse;
import com.business.i4_be.domain.user.dto.response.SignupResponse;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.global.jwt.TokenDto;
import com.business.i4_be.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        SignupResponse response = authService.signup(userSignupRequest);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody UserSigninRequest userSigninRequest) {
        SigninResponse response = authService.signin(userSigninRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken())
                .body(response);
    }

    // 탈퇴
    @DeleteMapping("/delete/me")
    public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.deleteMyAccount(userDetails);
        return ResponseEntity.ok("계정이 삭제되었습니다.");
    }

    // 특정 유저 삭제
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.deleteUser(userId, userDetails);
        return ResponseEntity.ok("유저 계정이 삭제되었습니다.");
    }
}
