package com.business.i4_be.domain.review.repository;

import com.business.i4_be.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>,ReviewRepositoryCustom {
    // 해당 가게의 리뷰 평균 평점 계산
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.store.storeId = :storeId")
    double findAverageRatingByStoreId(@Param("storeId") UUID storeId);
}

