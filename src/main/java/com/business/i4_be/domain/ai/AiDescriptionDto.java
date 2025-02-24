package com.business.i4_be.domain.ai;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiDescriptionDto {
    private UUID id;
    private String productName;
    private String generatedText;

    public AiDescriptionDto(AiDescription aiDescription) {
        this.id = aiDescription.getId();
        this.productName = aiDescription.getProductName();
        this.generatedText = aiDescription.getGeneratedText();
    }
}
