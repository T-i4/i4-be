package com.business.i4_be.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청에서 JWT 를 추출하고, 유효성 검증 후, SecurityContext 에 저장
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    // Token 유효성 검사, 사용자 정보 추출
    private final TokenProvider tokenProvider;
    // HTTP 요청 헤더에서 Token 찾을 때 사용하는 Key 값
    private static final String AUTHORIZATION_HEADER = "Authorization";
    // JWT 앞에 붙는 식별자
    private static final String BEARER_PREFIX = "Bearer ";

    // JWT 검증 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // JWT 를 가져와서 Token 유효성 검사
        // 유효하면 사용자 정보를 Spring Security 에 저장하고 다음 필터로 요청 전달
        String token = resolveToken(request);

        if (jwtUtil.validateToken(token).equals(TokenStatus.VALID)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Header 에서 JWT 를 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}