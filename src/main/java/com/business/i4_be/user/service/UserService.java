package com.business.i4_be.user.service;

import com.business.i4_be.user.dto.request.SignupRequest;
import com.business.i4_be.user.dto.response.SignupResponse;
import com.business.i4_be.user.entity.User;
import com.business.i4_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        log.info("회원가입 요청: {}", signupRequest); // 🔥 요청 데이터 확인

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(signupRequest.getPassword());
        log.info("암호화된 비밀번호: {}", encodePassword); // 🔥 비밀번호 확인

        // User 객체 생성
        User user = User.builder()
                .username(signupRequest.getUsername())
                .nickname(signupRequest.getNickname())
                .password(encodePassword)
                .email(signupRequest.getEmail())
                .phoneNumber(signupRequest.getPhoneNumber())
                .build();

        log.info("User 엔티티 생성: {}", user); // 🔥 저장 전 User 객체 확인

        // 저장
        userRepository.save(user);

        log.info("회원가입 완료: ID = {}", user.getUserId()); // 🔥 저장 완료 후 ID 확인

        return new SignupResponse(user.getUserId());
    }
}