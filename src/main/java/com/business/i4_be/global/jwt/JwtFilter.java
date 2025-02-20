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
        String requestURI = request.getRequestURI();  // 요청 URI 가져오기

        // 회원가입, 로그인 요청은 JWT 검증을 하지 않도록 예외 처리
        if (requestURI.equals("/api/v1/signup") || requestURI.equals("/api/v1/signin")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        if (token != null && jwtUtil.validateToken(token).equals(TokenStatus.VALID)) {
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