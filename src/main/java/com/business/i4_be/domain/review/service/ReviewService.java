package com.business.i4_be.domain.review.service;


import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.review.dto.ReviewReqDto;
import com.business.i4_be.domain.review.dto.ReviewResDto;
import com.business.i4_be.domain.review.entity.Review;
import com.business.i4_be.domain.review.repository.ReviewRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final OrderRepository orderRepository;

    public ReviewResDto.ReviewDto createReview(UUID orderId, ReviewReqDto.ReivewDto reviewReqDto) {

        return null;
    }

    //리뷰 수정
    @Transactional
    public ReviewResDto.ReviewDto updateReview(UUID reviewId, ReviewReqDto.UpdateReviewDto reqDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        review.updateReview(reqDto.getText(), reqDto.getRating());
        return ReviewResDto.ReviewDto.fromEntity(review);
    }

    @Transactional
    public void deleteReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 삭제자는 예시로 "system"
        review.delete("system");
        reviewRepository.save(review);

    }

    //사용자 ID로 리뷰 조회
    @Transactional(readOnly = true)
    public ReviewResDto.ReviewListDto getUserReviews(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return ReviewResDto.ReviewListDto.fromEntity(null, reviews); // storeId 필요 없음
    }

    //가게 ID로 리뷰 조회
    @Transactional(readOnly = true)
    public ReviewResDto.ReviewListDto getStoreReviews(UUID storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        return ReviewResDto.ReviewListDto.fromEntity(storeId, reviews);
    }
}
