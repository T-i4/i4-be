package com.business.i4_be.global.jwt;

import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;  // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;  // 7일

    public TokenDto generateTokens(String username, String authorities) {
        String accessToken = jwtUtil.generateToken(username, authorities, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.generateToken(username, authorities, REFRESH_TOKEN_EXPIRE_TIME);

        redisTemplate.opsForValue().set(username + ":refreshToken", refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return new TokenDto("Bearer", accessToken, refreshToken, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtUtil.parseClaims(token);

        // 권한 문자열 가져오기
        String role = claims.get("auth", String.class);
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER"; // 기본 권한
        }

        // ✅ 실제 DB에서 유저 조회
        User userEntity = userRepository.findByUsername(claims.getSubject())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + claims.getSubject()));

        // ✅ UserDetailsImpl 객체 생성 (role 문자열만 넘김)
        UserDetailsImpl principal = new UserDetailsImpl(userEntity, role);

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }



    public String refreshAccessToken(String refreshToken) {
        TokenStatus status = jwtUtil.validateToken(refreshToken);
        if (status != TokenStatus.VALID) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        String username = jwtUtil.parseClaims(refreshToken).getSubject();
        String storedRefreshToken = redisTemplate.opsForValue().get(username + ":refreshToken");

        if (!refreshToken.equals(storedRefreshToken)) {
            throw new IllegalArgumentException("Refresh Token mismatch");
        }

        return jwtUtil.generateToken(username, "ROLE_USER", ACCESS_TOKEN_EXPIRE_TIME);
    }
}
