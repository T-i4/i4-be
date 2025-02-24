package com.business.i4_be.domain.product.repository;

import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.store.entity.Store;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  Optional<Product> findByProductId(UUID productId);

  Optional<Product> findByProductIdAndStore_storeId(UUID productId, UUID storeId);

  Page<Product> findByStore_storeIdAndProductNameContaining(UUID storeId, String keyword, Pageable pageable);

  List<Product> findByStore(Store store);

  boolean existsByProductName(String productName);

  Optional<Product> findByProductName( String productName);

  List<Product> findAllByProductIdIn(List<UUID> productIds);
}
