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


                        // 수정
                        .requestMatchers("/api/v1/users/update/me").authenticated()

                        // 탈퇴
                        .requestMatchers("/api/v1/delete/me").authenticated()
                        .requestMatchers("/api/v1/delete/{userId}").hasAnyAuthority("ROLE_MASTER", "ROLE_ADMIN")

                        // --- 장바구니 ----
                        .requestMatchers("/api/v1/cart/**").authenticated()
                        .requestMatchers("/api/v1/cart/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_MASTER", "ROLE_ADMIN")


                        // --- 가게 ----
                        .requestMatchers(HttpMethod.POST,"/api/owner/v1/stores")
                        .hasAnyAuthority("ROLE_ADMIN","ROLE_MASTER")
                        .requestMatchers(HttpMethod.DELETE,"/api/owner/v1/stores/{storeId}")
                        .hasAnyAuthority("ROLE_MASTER")
                        .requestMatchers(HttpMethod.PUT,"/api/owner/v1/stores/{storeId}")
                        .hasAnyAuthority("ROLE_OWNER","ROLE_ADMIN","ROLE_MASTER")
                        .requestMatchers(HttpMethod.PATCH,"/api/owner/v1/{storeId}/status","/api/owner/v1/{storeId}/category")
                        .hasAnyAuthority("ROLE_OWNER","ROLE_ADMIN","ROLE_MASTER")
                        .requestMatchers("/api/owner/v1/stores/**").authenticated()




                        .requestMatchers("/api/v1/stores/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_MASTER","ROLE_OWNER","ROLE_ADMIN")
                        .requestMatchers("/api/v1/stores/**").authenticated()
                       .anyRequest().authenticated()
                )
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class) // CORS 필터
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class) // 예외 필터
                .addFilterAfter(new JwtFilter(jwtUtil, tokenProvider), UsernamePasswordAuthenticationFilter.class) // 🔥 JwtFilter 실행 순서 변경
                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
