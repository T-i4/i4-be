package com.business.i4_be.domain.ai;

import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiDescriptionService {

    private final AiApiService aiApiService;
    private final AiDescriptionRepository aiDescriptionRepository;
    private final UserRepository userRepository;
    @Transactional
    public String generateAndSaveDescription(Long userId, String productName) {
        User user =userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 이미 존재하는 설명인지 확인
        return aiDescriptionRepository.findByProductName(productName)
                .map(AiDescription::getGeneratedText)
                .orElseGet(() -> {
                    // Google Gemini API 호출하여 설명 생성
                    String generatedText = aiApiService.generateProductDescription(productName);

                    // 설명을 DB에 저장
                    AiDescription aiDescription = AiDescription.create(productName, generatedText, user);
                    aiDescriptionRepository.save(aiDescription);

                    return generatedText;
                });
    }
    @Transactional(readOnly = true)
    public  List<AiDescriptionDto> getUserDescriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user.getAiDescriptions().stream()
                .map(AiDescriptionDto::new)
                .collect(Collectors.toList());
    }
}
