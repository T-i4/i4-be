package com.business.i4_be.domain.ai;


import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name ="p_descriptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiDescription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_description_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;

    @Column(name = "generated_text", nullable = false, length = 255)
    private String generatedText; // AI가 생성한 설명 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static AiDescription create(String productName, String generatedText, User user) {
        return AiDescription.builder()
                .productName(productName)
                .generatedText(generatedText)
                .user(user)
                .build();
    }

}
