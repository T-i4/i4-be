package com.business.i4_be.domain.review.service;


import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.review.dto.PagedResDto;
import com.business.i4_be.domain.review.dto.ReviewReqDto;
import com.business.i4_be.domain.review.dto.ReviewResDto;
import com.business.i4_be.domain.review.entity.Review;
import com.business.i4_be.domain.review.repository.ReviewRepository;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResDto.ReviewDto createReview(UUID orderId, ReviewReqDto.ReviewDto reviewReqDto,Long userId) {

        // 주문 가져오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        //주문 유저 가져오기
        User user =order.getUser();
        //주문이 속한 Store
        Store store = order.getStore();

        // 주문 상태가 COMPLETED인지 확인
        if (!OrderStatus.COMPLETED.equals(order.getOrderStatus())) {
            throw new CustomException(ErrorCode.ORDER_NOT_COMPLETED);
        }

        // 리뷰 생성
        Review review = Review.builder()
                .user(user)
                .store(store)
                .order(order)
                .rating(reviewReqDto.getRating())
                .text(reviewReqDto.getText())
                .build();

        reviewRepository.save(review);
        return ReviewResDto.ReviewDto.fromEntity(review);
    }


    //리뷰 수정
    @Transactional
    public ReviewResDto.ReviewDto updateReview(
            UUID reviewId, ReviewReqDto.UpdateReviewDto reqDto,Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        review.updateReview(reqDto.getText(), reqDto.getRating());
        return ReviewResDto.ReviewDto.fromEntity(review);
    }

    @Transactional
    public void deleteReview(UUID reviewId,Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 삭제자는 예시로 "system"
        review.delete(userId.toString());
        reviewRepository.save(review);

    }

    //사용자 ID로 리뷰 조회
    @Transactional(readOnly = true)
    public PagedResDto<ReviewResDto.ReviewDto> getUserReviews(Long userId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByUserIdWithPagination(userId, pageable);
        return new PagedResDto<>(reviews.map(ReviewResDto.ReviewDto::fromEntity));
    }



    @Transactional(readOnly = true)
    public PagedResDto<ReviewResDto.ReviewDto> getReviewsByStore(UUID storeId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByStoreIdWithPagination(storeId, pageable);
        return new PagedResDto<>(reviews.map(ReviewResDto.ReviewDto::fromEntity));
    }
}
