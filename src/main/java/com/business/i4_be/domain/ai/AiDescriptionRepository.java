package com.business.i4_be.domain.ai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiDescriptionRepository extends JpaRepository<AiDescription, Long> {
    Optional<AiDescription> findByProductName(String productName);
    //설명 이미 있으면 API호출 x DB에서 바로 반환
}