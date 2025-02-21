package com.business.i4_be.domain.cart.repository;

import com.business.i4_be.domain.cart.entity.Cart;
import com.business.i4_be.domain.user.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

  Cart findByUser(User user);

  Cart findByCartIdAndUserId(UUID cartId, Long userId);

}
