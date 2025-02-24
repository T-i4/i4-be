package com.business.i4_be.domain.ai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AiDescriptionRepository extends JpaRepository<AiDescription, UUID> {
    Optional<AiDescription> findByProductName(String productName);
    //설명 이미 있으면 API호출 x DB에서 바로 반환
    List<AiDescription> findByUserId(Long userId);

}