package com.business.i4_be.domain.review.repository;

import com.business.i4_be.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByUserId(Long userId);


    List<Review> findByStore_StoreId(UUID storeId);
}
