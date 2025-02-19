package com.business.i4_be.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

// 토큰 생성 및 검증
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    // token 의 무결성을 보장하기 위해 HMAC SHA256 알고리즘 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Base64 인코딩된 Secret Key 를 디코딩하여 바이너리 데이터로 변환
    // 디코딩한 데이터를 사용하여 JWT 서명용 Key 객체 생성
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    // Token 생성 (username 과 Token 만료 시간 설정)
    public String generateToken(String subject, long expirationMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // Token 에서 정보 추출 (Token 이 만료되면 예외 대신 Claims 를 반환하여 User 에게 알림)
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Token 검증
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            log.error("토큰이 유효하지 않습니다.");
            return TokenStatus.INVALID;
        }
    }
}
