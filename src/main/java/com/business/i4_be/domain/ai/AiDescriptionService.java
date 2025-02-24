package com.business.i4_be.domain.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiDescriptionService {

    private final AiApiService aiApiService;
    private final AiDescriptionRepository aiDescriptionRepository;
    @Transactional
    public String generateAndSaveDescription(String productName) {
        // 이미 존재하는 설명인지 확인
        return aiDescriptionRepository.findByProductName(productName)
                .map(AiDescription::getGeneratedText)
                .orElseGet(() -> {
                    // Google Gemini API 호출하여 설명 생성
                    String generatedText = aiApiService.generateProductDescription(productName);

                    // 설명을 DB에 저장
                    AiDescription aiDescription = AiDescription.create(productName, generatedText);
                    aiDescriptionRepository.save(aiDescription);

                    return generatedText;
                });
    }
}
