package com.business.i4_be.global.config;

import com.business.i4_be.domain.user.security.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty(); // 로그인되지 않은 경우 처리
        }

        Object principal = authentication.getPrincipal();

        // principal이 UserDetailsImpl이면 username 반환, 아니라면 String(username) 반환
        if (principal instanceof UserDetailsImpl) {
            return Optional.of(((UserDetailsImpl) principal).getUsername());
        } else if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.empty();
    }

}
