package com.business.i4_be.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Access Token, Refresh Token 생성 및 검증
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;  // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;  // 7일

    public TokenDto generateTokens(String username, String authorities) {
        String accessToken = jwtUtil.generateToken(username, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.generateToken(username, REFRESH_TOKEN_EXPIRE_TIME);

        redisTemplate.opsForValue().set(username + ":refreshToken", refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return new TokenDto("Bearer", accessToken, refreshToken, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // JWT 에서 Claims 가져와서 User 정보 추출
    // Spring Security 의 User 객체 생성하여 SecurityContext 에 저장되면 인증된 User 로 간주
    public Authentication getAuthentication(String token) {
        Claims claims = jwtUtil.parseClaims(token);
        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth", String.class).split(","))
                .map(SimpleGrantedAuthority::new).toList();

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Redis 에 저장된 가져와서 Refresh Token 검증 후 일치하면 Access Token 재발급
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

        return jwtUtil.generateToken(username, ACCESS_TOKEN_EXPIRE_TIME);
    }
}