package com.business.i4_be.domain.user.service;

import com.business.i4_be.domain.user.dto.request.SigninRequest;
import com.business.i4_be.domain.user.dto.request.SignupRequest;
import com.business.i4_be.domain.user.dto.request.UserSigninRequest;
import com.business.i4_be.domain.user.dto.request.UserSignupRequest;
import com.business.i4_be.domain.user.dto.response.SigninResponse;
import com.business.i4_be.domain.user.dto.response.SignupResponse;
import com.business.i4_be.domain.user.dto.response.UserResponse;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import com.business.i4_be.global.jwt.TokenDto;
import com.business.i4_be.global.jwt.TokenProvider;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    public SignupResponse signup(final UserSignupRequest usersignupRequest) {

        // user 필드
        SignupRequest request = usersignupRequest.getUser();

        // 중복 검증
        validateDuplicateUser(request.getUsername());

        // 역할 검증 (USER 또는 OWNER만 가능)
        UserRole role;

        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 역할입니다. USER 또는 OWNER만 가능합니다.");
        }

        if (role == UserRole.ADMIN || role == UserRole.MASTER) {
            throw new IllegalArgumentException("관리자 계정은 회원가입할 수 없습니다.");
        }

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .password(encodePassword)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .build();

        // DB 저장
        userRepository.save(user);

        // 회원가입 응답 반환
        return new SignupResponse("회원가입이 완료되었습니다.", user);
    }

    // 중복 검증
    private void validateDuplicateUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }

    public SigninResponse signin(UserSigninRequest userSigninRequest) {
        // user 필드
        SigninRequest request = userSigninRequest.getUser();

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokens(user.getUsername(), user.getRole().name());

        return new SigninResponse("로그인 성공", tokenDto, user);
    }

    // 탈퇴
    public void deleteMyAccount(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

    // 특정 유저 삭제
    public void deleteUser(Long userId, UserDetailsImpl userDetails) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        UserRole requesterRole = userDetails.getUser().getRole();
        UserRole targetRole = targetUser.getRole();

        // ADMIN은 MASTER 삭제 불가
        if (requesterRole == UserRole.ADMIN && targetRole == UserRole.MASTER) {
            throw new AccessDeniedException("ADMIN은 MASTER 계정을 삭제할 수 없습니다.");
        }

        // MASTER는 모두 삭제 가능
        // ADMIN은 MASTER만 제외하고 삭제 가능
        if (requesterRole == UserRole.MASTER || requesterRole == UserRole.ADMIN) {
            userRepository.delete(targetUser);
        } else {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
    }
}