package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.global.jwt.JwtUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 내 정보 조회
    public UserResponse getMyInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // username 으로 사용자 정보 검색
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return new UserResponse(user);
    }

    // 모든 사용자 조회
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }


    // 특정 유저 조회
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    // 정보 수정
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 입력된 값이 기존 값과 다를 경우에만 업데이트
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            user.updateUsername(request.getUsername());
        }
        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            user.updateNickname(request.getNickname());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            user.updateEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.updatePhoneNumber(request.getPhoneNumber());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.updatePassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        }

        userRepository.save(user);
        return new UserResponse(user);
    }

}