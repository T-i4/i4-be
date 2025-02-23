package com.business.i4_be.domain.store.repository;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StoreRepositoryCustom {


    Page<Store> findAllWithPagination(Long userId, Pageable pageable);
    Page<Store> findByUserIdWithPagination(Long userId, Pageable pageable);
    Page<Store> findByCategoryWithPagination(StoreCategory category, Long userId, Pageable pageable);

    Page<Store> findByKeywordWithPagination(String keyword, Long userId, Pageable pageable);
}
