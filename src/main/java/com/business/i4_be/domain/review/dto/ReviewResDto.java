package com.business.i4_be.domain.review.dto;

import com.business.i4_be.domain.review.entity.Review;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ReviewResDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDto {
        private UUID reviewId;
        private UUID storeId;
        private Long userId;
        private String text;
        private int rating;
        public static ReviewDto fromEntity(Review review) {
            return ReviewDto.builder()
                    .reviewId(review.getReviewId())
//                    .storeId(review.getStore().getStoreId())
                    .storeId(review.getStore() != null ? review.getStore().getStoreId() : null)
//                    .userId(review.getUser().getUserId())
                    .userId(review.getUser() != null ? review.getUser().getUserId() : null)  // ✅ Null 체크 추가
                    .rating(review.getRating())
                    .text(review.getText())
                    .build();
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ReviewListDto {
        private UUID storeId;
        private List<ReviewDto> reviews;

        public static ReviewListDto fromEntity(UUID storeId, List<Review> reviewList) {
            return ReviewListDto.builder()
                    .storeId(storeId)
                    .reviews(reviewList.stream()
                            .map(ReviewDto::fromEntity)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
