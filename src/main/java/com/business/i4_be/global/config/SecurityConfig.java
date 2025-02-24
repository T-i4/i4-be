package com.business.i4_be.global.config;

import com.business.i4_be.global.exception.CustomAccessDeniedHandler;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth

                        // 회원가입, 로그인
                        .requestMatchers("/api/v1/signin", "/api/v1/signup").permitAll()

                        // 조회
                        .requestMatchers("/api/v1/users/me").authenticated()
                        .requestMatchers("/api/v1/users/all").hasAnyAuthority("MASTER", "ADMIN")
                        .requestMatchers("/api/v1/users/{userId}").hasAnyAuthority("MASTER", "ADMIN")

                        // 수정
                        .requestMatchers("/api/v1/users/update/me").authenticated()

                        // 탈퇴
                        .requestMatchers("/api/v1/delete/me").authenticated()
                        .requestMatchers("/api/v1/delete/{userId}").hasAnyAuthority("MASTER", "ADMIN")

                        // --- 장바구니 ----
                        .requestMatchers("/api/v1/cart/**").authenticated()
                        .requestMatchers("/api/v1/cart/**")
                        .hasAnyAuthority("USER", "MASTER", "ADMIN")


                        // --- 가게 ----
                        .requestMatchers(HttpMethod.POST,"/api/owner/v1/stores")
                        .hasAnyAuthority("ADMIN","MASTER")
                        .requestMatchers(HttpMethod.DELETE,"/api/owner/v1/stores/{storeId}")
                        .hasAnyAuthority("MASTER")
                        .requestMatchers(HttpMethod.PUT,"/api/owner/v1/stores/{storeId}")
                        .hasAnyAuthority("OWNER","ADMIN","MASTER")
                        .requestMatchers(HttpMethod.PATCH,"/api/owner/v1/{storeId}/status","/api/owner/v1/{storeId}/category")
                        .hasAnyAuthority("OWNER","ADMIN","MASTER")
                        .requestMatchers("/api/owner/v1/stores/**").authenticated()


                        .requestMatchers("/api/v1/stores/**")
                        .hasAnyAuthority("USER", "MASTER","OWNER","ADMIN")
                        .requestMatchers("/api/v1/stores/**").authenticated()


                        // Product
                        .requestMatchers("/api/owner/v1/products").hasAnyAuthority("MASTER", "ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.GET, "api/v1/products/**","/api/v1/products").permitAll()
                        .requestMatchers("/api/v1/products/**","/api/v1/products").hasAnyAuthority("MASTER", "ADMIN", "OWNER")


                        //리뷰

                        .requestMatchers("/api/v1/reviews/orders/{orderId}")
                        .hasAnyAuthority("USER")
                        .requestMatchers("/api/v1/reviews/{reviewId}")
                        .hasAnyAuthority("USER")
                        .requestMatchers("/api/v1/reviews/{reviewId}")
                        .hasAnyAuthority("USER","ADMIN","MASTER")
                        .requestMatchers("/api/v1/reviews/users/{userId}")
                        .hasAnyAuthority("USER","ADMIN","MASTER")
                        .requestMatchers("/api/v1/stores/{storeId}/reviews")
                        .hasAnyAuthority("USER", "MASTER","OWNER","ADMIN")
                        .requestMatchers("/api/v1/reviews/**").authenticated()
                        .anyRequest().authenticated()


                )

                // 🔹 Custom AccessDeniedHandler 추가
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // CORS -> 예외처리 -> JWT 순서로 필터 적용
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil, tokenProvider), UsernamePasswordAuthenticationFilter.class)

                // 기본 로그인 폼, HTTP Basic 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
