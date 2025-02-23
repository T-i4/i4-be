package com.business.i4_be.global.config;

import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initAdminUser() {
        return args -> {

            if (userRepository.findByUsername("MASTER").isEmpty()) {
                User master = User.builder()
                        .username("MASTER")
                        .password(passwordEncoder.encode("Master1234!"))
                        .nickname("마스터")
                        .email("master@auto.com")
                        .phoneNumber("01011112222")
                        .role(UserRole.MASTER)
                        .build();
                userRepository.save(master);
            }

            if (userRepository.findByUsername("ADMIN").isEmpty()) {
                User admin = User.builder()
                        .username("ADMIN")
                        .password(passwordEncoder.encode("Admin1234!"))
                        .nickname("어드민")
                        .email("admin@auto.com")
                        .phoneNumber("01000000000")
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}