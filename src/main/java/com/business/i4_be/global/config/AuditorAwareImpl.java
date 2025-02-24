package com.business.i4_be.global.config;

import com.business.i4_be.domain.user.security.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty(); // 로그인되지 않은 경우 처리
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Optional.of(userDetails.getUsername()); // 현재 로그인된 유저의 ID or Username 반환
    }

}
