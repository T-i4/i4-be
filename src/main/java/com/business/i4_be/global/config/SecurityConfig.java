package com.business.i4_be.global.config;

import com.business.i4_be.global.exception.ExceptionHandlerFilter;
import com.business.i4_be.global.jwt.JwtFilter;
import com.business.i4_be.global.jwt.JwtUtil;
import com.business.i4_be.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CorsConfig corsConfig;
    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth

                        // 회원가입, 로그인
                        .requestMatchers("/api/v1/signin", "/api/v1/signup").permitAll()

                        // 조회
                        .requestMatchers("/api/v1/users/me").authenticated()
                        .requestMatchers("/api/v1/users/all").hasAnyAuthority("ROLE_MASTER", "ROLE_ADMIN")
                        .requestMatchers("/api/v1/users/{userId}").hasAnyAuthority("ROLE_MASTER", "ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class) // CORS 필터 적용
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class) // 예외 필터 적용
                .addFilterBefore(new JwtFilter(jwtUtil, tokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
