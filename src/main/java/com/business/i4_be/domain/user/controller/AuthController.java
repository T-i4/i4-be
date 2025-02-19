package com.business.i4_be.domain.user.controller;

import com.business.i4_be.domain.user.dto.request.SigninRequest;
import com.business.i4_be.global.jwt.TokenDto;
import com.business.i4_be.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestBody SigninRequest signinRequest) {
        TokenDto tokenDto = authService.signin(signinRequest);
        return ResponseEntity.ok(tokenDto);
    }
}
