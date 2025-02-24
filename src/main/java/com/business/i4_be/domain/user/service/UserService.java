package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.address.repository.AddressRepository;
import com.business.i4_be.domain.user.dto.request.UpdateUserRequest;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.dto.response.UserResponseWrapper;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import com.business.i4_be.global.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.addressRepository = addressRepository;
    }

    // 내 정보 조회
    public UserResponseWrapper getMyInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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

        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            List<Address> newAddresses = request.getAddresses().stream()
                    .map(addressReq -> addressReq.toEntity(user)) // AddressRequest → Address 엔티티 변환
                    .collect(Collectors.toList());

            user.updateAddresses(newAddresses);
        }

        userRepository.save(user);
        return new UserResponseWrapper(new UserResponse(user));
    }

    // 주소 삭제
    public UserResponseWrapper deleteUserAddress(Long userId, UUID addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.deleteAddress(address);
        addressRepository.delete(address);

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

    // 회원 삭제
    public void deleteUser(Long userId, Long loggedInUserId) {
        User loggedInUser = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // MASTER는 누구든 삭제 가능
        if (loggedInUser.getRole() == UserRole.MASTER) {
            userRepository.delete(targetUser);
            return;
        }

        // ADMIN은 MASTER 제외 삭제 가능
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            if (targetUser.getRole() != UserRole.MASTER) {
                userRepository.delete(targetUser);
                return;
            } else {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ACTION);
            }
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_ACTION);
    }
}