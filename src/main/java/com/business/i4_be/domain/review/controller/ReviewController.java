package com.business.i4_be.domain.review.controller;

import com.business.i4_be.domain.review.dto.ReviewReqDto;
import com.business.i4_be.domain.review.dto.ReviewResDto;
import com.business.i4_be.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private ReviewService reviewService;

    //리뷰 생성
    @PostMapping("/orders/{orderId}/review")
    public ResponseEntity<ReviewResDto.ReviewDto> createReview(
            @PathVariable UUID orderId,
            @Valid @RequestBody ReviewReqDto.ReivewDto reviewReqDto) {

        return ResponseEntity.ok(reviewService.createReview(orderId, reviewReqDto));
    }

    //리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResDto.ReviewDto> updateReview(@PathVariable UUID reviewId, @Valid @RequestBody ReviewReqDto.UpdateReviewDto updateReqDto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, updateReqDto));
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 사용자 ID로 본인이 작성한 리뷰 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ReviewResDto.ReviewListDto> getUserReviews(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }

    // 가게 ID로 해당 가게 리뷰 조회 (리스트 응답)
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ReviewResDto.ReviewListDto> getStoreReviews(@PathVariable UUID storeId) {
        return ResponseEntity.ok(reviewService.getStoreReviews(storeId));
    }
}

