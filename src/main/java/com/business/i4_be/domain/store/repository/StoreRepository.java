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
    List<Store> findByCategory(StoreCategory category);



    List<Store> findByStoreNameContaining(String keyword);


    boolean existsByStoreName(String storeName);



    //가게 단건 조회 (ALL)
    @Override
    Optional<Store> findById(UUID storeId);

    //가게 단건 조회(OWNER)
    Optional<Store>findByStoreIdAndUserId(UUID storeId, Long userId);
    //가게 목록 조회(ALL)
    List<Store> findAll();
    //가게 목록 조회(OWNER)
    List<Store> findAllByUserId(Long userId);




}
