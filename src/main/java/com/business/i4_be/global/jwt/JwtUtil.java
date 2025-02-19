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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    // ✅ Token 생성 시 권한 값이 없으면 기본값("ROLE_USER")을 추가
    public String generateToken(String subject, String authorities, long expirationMs) {
        if (authorities == null || authorities.isEmpty()) {
            authorities = "ROLE_USER";  // 기본 권한 추가
        }

        return Jwts.builder()
                .setSubject(subject)
                .claim("auth", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // ✅ Token 에서 정보 추출할 때 auth 값이 없으면 기본값 부여
    public Claims parseClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            // auth 클레임이 없거나 비어있다면 기본값 추가
            if (!claims.containsKey("auth") || claims.get("auth", String.class) == null) {
                claims.put("auth", "ROLE_USER");
            }

            return claims;
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // ✅ Token 검증 시 상세한 예외 처리 추가
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
            return TokenStatus.EXPIRED;
        } catch (MalformedJwtException e) {
            log.error("잘못된 형식의 JWT 입니다.");
            return TokenStatus.INVALID;
        } catch (SignatureException e) {
            log.error("JWT 서명이 유효하지 않습니다.");
            return TokenStatus.INVALID;
        } catch (JwtException e) {
            log.error("JWT 검증 중 오류가 발생했습니다.");
            return TokenStatus.INVALID;
        }
    }
}
