package com.business.i4_be.domain.ai;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReqDto {
    private List<Content> contents;

    public static AiReqDto of(String productName) {
        // 글자 수 제한 (최대 50자)
        if (productName.length() > 50) {
            productName = productName.substring(0, 50);
        }
        String prompt = productName + " 상품의 설명이 필요합니다. 답변을 최대한 간결하게 50자 이하로 작성해 주세요.";
        return new AiReqDto(Collections.singletonList(new Content(Collections.singletonList(new Part(prompt)))));
    };
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
