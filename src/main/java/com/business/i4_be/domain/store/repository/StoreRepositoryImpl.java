package com.business.i4_be.domain.store.repository;


import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.entity.QStore;
import com.business.i4_be.domain.store.entity.Store;
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

@Repository
@Primary
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;


    //전체 가게 조회 (페이징 적용)
    @Override
    public Page<Store> findAllWithPagination(Long userId, Pageable pageable) {
        JPAQuery<Store> query = queryFactory
                .selectFrom(store)
                .where(store.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.createdAt.desc());

        return getPagedResult(query, pageable);
    }

    //특정 OWNER의 가게 조회 (페이징 적용)

    @Override
    public Page<Store> findByUserIdWithPagination(Long userId, Pageable pageable) {
        JPAQuery<Store> query = queryFactory
                .selectFrom(store)
                .where(store.user.userId.eq(userId), isNotDeleted())
                .orderBy(getSort(pageable));

        return getPagedResult(query, pageable);
    }

    //가게 이름 검색 (페이징 적용)

    @Override
    public Page<Store> findByKeywordWithPagination(String keyword,Long userId, Pageable pageable) {
        JPAQuery<Store> query = queryFactory
                .selectFrom(store)
                .where(
                        store.storeName.containsIgnoreCase(keyword),
                        store.deletedAt.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.createdAt.desc());

        return getPagedResult(query, pageable);
    }

    //가게 카테고리별 검색 (페이징 적용)

    @Override
    public Page<Store> findByCategoryWithPagination(StoreCategory category, Long userId,Pageable pageable) {
        JPAQuery<Store> query = queryFactory
                .selectFrom(store)
                .where(
                        store.category.eq(category),
                        store.deletedAt.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.createdAt.desc());

        return getPagedResult(query, pageable);
    }

    //QueryDSL 정렬 기준 설정 (기본: createdAt DESC)
    private OrderSpecifier<?> getSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                if (order.getProperty().equals("updatedAt")) {
                    return order.isAscending() ? store.updatedAt.asc() : store.updatedAt.desc();
                }
            }
        }
        return store.createdAt.desc(); // 기본 정렬 (createdAt 내림차순)
    }

    /**
     * 삭제된 데이터 제외 (Soft Delete 적용)
     */
    private BooleanExpression isNotDeleted() {
        return store.deletedAt.isNull();
    }

    //페이징 결과 반환 (QueryDSL)

    private Page<Store> getPagedResult(JPAQuery<Store> query, Pageable pageable) {
        List<Store> stores = query.fetch();
        long totalCount = queryFactory.select(store.count()).from(store).fetchFirst();
        return new PageImpl<>(stores, pageable, totalCount);
    }

}
