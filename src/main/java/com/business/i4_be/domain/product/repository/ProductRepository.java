package com.business.i4_be.domain.product.repository;

import com.business.i4_be.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  Optional<Product> findByProductId(UUID productId);

  Optional<Product> findByProductIdAndStore_storeId(UUID productId, UUID storeId);

  List<Product> findByStore_storeIdAndProductNameContaining(UUID storeId, String keyword);

  List<Product> findByStore_storeId(UUID storeId);

  boolean existsByProductName(String productName);

  Optional<Product> findByProductName( String productName);
}
