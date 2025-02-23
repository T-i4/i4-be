package com.business.i4_be.domain.review.controller;

import com.business.i4_be.domain.review.dto.PagedResDto;
import com.business.i4_be.domain.review.dto.ReviewReqDto;
import com.business.i4_be.domain.review.dto.ReviewResDto;
import com.business.i4_be.domain.review.service.ReviewService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성
    @PostMapping("/reviews/orders/{orderId}")
    public ResponseEntity<ReviewResDto.ReviewDto> createReview(
            @PathVariable UUID orderId,
            @Valid @RequestBody ReviewReqDto.ReviewDto reviewReqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(reviewService.createReview(orderId, reviewReqDto, userDetails.getUser().getId()));
    }


    //리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResDto.ReviewDto> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewReqDto.UpdateReviewDto updateReqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(reviewService.updateReview(reviewId, updateReqDto, userDetails.getUser().getId()));
    }

    //리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    // 사용자 ID로 본인이 작성한 리뷰 조회
    @GetMapping("/reviews/users/{userId}")
    public ResponseEntity<PagedResDto<ReviewResDto.ReviewDto>> getUserReviews(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "updatedAt") Pageable pageable) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId, pageable));
    }

    //가게별 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<PagedResDto<ReviewResDto.ReviewDto>> getReviewsByStore(
            @PathVariable UUID storeId,
            @PageableDefault(size = 10, sort = "updatedAt") Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByStore(storeId, pageable));
    }
}



