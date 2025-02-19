package com.business.i4_be.domain.store.repository;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findAll();

    List<Store> findByCategory(StoreCategory category);



    List<Store> findByStoreNameContaining(String keyword);


    boolean existsByStoreName(String storeName);

}
