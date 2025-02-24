package com.business.i4_be.domain.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "GEMINI API")
@Service
@RequiredArgsConstructor
public class AiApiService {
    private final RestTemplate restTemplate;

    @Value("${google.api.key}") // API 키 로드
    private String apiKey;

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    public String generateProductDescription(String productName) {
        // 요청 URL 생성
        URI uri = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("key", apiKey)
                .build()
                .toUri();
        log.info("Gemini API 요청 URL: {}", uri);

        // 요청 본문 생성
        AiReqDto requestDto = AiReqDto.of(productName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AiReqDto> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            //API호출 (POST)
            ResponseEntity<AiResDto> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, AiResDto.class);
            log.info("Gemini API 응답 상태 코드: {}", responseEntity.getStatusCode());

            //AI 응답에서 설명만 추출 후 반환
            return responseEntity.getBody() != null ? responseEntity.getBody().extractText() : "추천 설명을 생성할 수 없습니다.";
        } catch (Exception e) {
            log.error("Gemini API 호출 오류: {}", e.getMessage());
            return "추천 설명을 생성할 수 없습니다.";
        }
    }

}
