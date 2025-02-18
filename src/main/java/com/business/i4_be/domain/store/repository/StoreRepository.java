package com.business.i4_be.domain.store.repository;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByStoreIdAndIsDeletedFalse(UUID storeId);

    List<Store> findAllByIsDeletedFalse(); //삭제되지 않은 데이터 조회(기본 조회)

    List<Store> findByStoreNameContaining(String keyword);
    List<Store> findByCategory(StoreCategory category);

    // 검색 기능 (삭제되지 않은 데이터만)
    List<Store> findByStoreNameContainingAndIsDeletedFalse(String keyword);
    List<Store> findByCategoryAndIsDeletedFalse(StoreCategory category);

    List<Store> findAllByIsDeletedTrue();

    boolean existsByStoreName(String storeName);
}
