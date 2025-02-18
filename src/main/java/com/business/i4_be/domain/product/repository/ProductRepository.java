package com.business.i4_be.domain.product.repository;

import com.business.i4_be.domain.product.entity.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  Optional<Product> findByProductId(UUID productId);
}
