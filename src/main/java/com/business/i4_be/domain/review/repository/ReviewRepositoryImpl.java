package com.business.i4_be.domain.review.repository;


import com.business.i4_be.domain.review.entity.QReview;
import com.business.i4_be.domain.review.entity.Review;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QReview review = QReview.review;

    @Override
    public Page<Review> findByUserIdWithPagination(Long userId, Pageable pageable) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .where(review.user.userId.eq(userId), isNotDeleted())
                .orderBy(getSort(pageable));

        return getPagedResult(query, pageable);
    }

    @Override
    public Page<Review> findByStoreIdWithPagination(UUID storeId, Pageable pageable) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .where(review.store.storeId.eq(storeId), isNotDeleted())
                .orderBy(getSort(pageable));

        return getPagedResult(query, pageable);
    }

    //QueryDSL 정렬 기준 설정 (기본: createdAt DESC)
    private OrderSpecifier<?> getSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                if (order.getProperty().equals("updatedAt")) {
                    return order.isAscending() ? review.updatedAt.asc() : review.updatedAt.desc();
                }
            }
        }
        return review.createdAt.desc(); // 기본 정렬 (createdAt 내림차순)
    }

    //삭제된 데이터 제외 (Soft Delete 적용)
    private BooleanExpression isNotDeleted() {
        return review.deletedAt.isNull();
    }

    //페이징 결과 반환 (QueryDSL)
    private Page<Review> getPagedResult(JPAQuery<Review> query, Pageable pageable) {
        List<Review> reviews = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(review.count())
                .from(review)
                .where(isNotDeleted())
                .fetchFirst();

        return new PageImpl<>(reviews, pageable, totalCount);
    }

}
