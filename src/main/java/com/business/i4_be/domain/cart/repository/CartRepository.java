package com.business.i4_be.domain.cart.repository;

import com.business.i4_be.domain.cart.entity.Cart;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
}
