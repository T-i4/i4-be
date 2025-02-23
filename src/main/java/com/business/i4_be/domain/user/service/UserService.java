package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.request.UserUpdateWrapper;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.dto.response.UserResponseWrapper;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import com.business.i4_be.global.jwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public UserResponseWrapper getMyInfo(User user) {
        return new UserResponseWrapper(new UserResponse(user));
    }

    // 모든 사용자 조회
    public List<UserResponseWrapper> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseWrapper(new UserResponse(user)))
                .collect(Collectors.toList());
    }

    // 특정 유저 조회
    public UserResponseWrapper getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseWrapper(new UserResponse(user));
    }

    // 정보 수정
    public UserResponseWrapper updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // role 변경 방지
        if (request.getRole() != null) {
            throw new CustomException(ErrorCode.ROLE_UPDATE_NOT_ALLOWED);
        }

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
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }

        // 주소 update
        if (request.getAddress() != null) {
            user.updateAddress(request.getAddress());
        }

        userRepository.save(user);
        return new UserResponseWrapper(new UserResponse(user));
    }

    // 주소 삭제
    public UserResponseWrapper deleteUserAddress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.deleteAddress();

        userRepository.save(user);
        return new UserResponseWrapper(new UserResponse(user));
    }

    // 회원 탈퇴
    public Map<String, String> deleteMyAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "탈퇴되었습니다.");
        return response;
    }
}