package com.business.i4_be.domain.cart.repository;

import com.business.i4_be.domain.cart.entity.ProductCart;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, UUID> {

}
