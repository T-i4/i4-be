package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.user.dto.request.SigninRequest;
import com.business.i4_be.domain.user.dto.request.SignupRequest;
import com.business.i4_be.domain.user.dto.response.SignupResponse;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.global.jwt.TokenDto;
import com.business.i4_be.global.jwt.TokenProvider;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public SignupResponse signup(final SignupRequest request) {

        // 중복 검증
        validateDuplicateUser(request.getUsername());

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .password(encodePassword)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(UserRole.USER)
                .build();

        // DB 저장
        userRepository.save(user);

        return new SignupResponse(user.getUserId());
    }

    // 중복 검증
    private void validateDuplicateUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }

    public TokenDto signin(SigninRequest signinRequest) {
        User user = userRepository.findByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String authorities = user.getRole().name(); // UserRole Enum을 문자열로 변환
        return tokenProvider.generateTokens(user.getUsername(), authorities);
    }
}