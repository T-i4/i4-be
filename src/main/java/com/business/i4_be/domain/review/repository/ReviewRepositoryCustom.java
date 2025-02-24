package com.business.i4_be.domain.review.repository;

import com.business.i4_be.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewRepositoryCustom {
    Page<Review> findByUserIdWithPagination(Long userId, Pageable pageable);
    Page<Review> findByStoreIdWithPagination(UUID storeId, Pageable pageable);
}
