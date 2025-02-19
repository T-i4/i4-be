package com.business.i4_be.domain.cart.service;

import com.business.i4_be.domain.cart.dto.AddCartReqDto;
import com.business.i4_be.domain.cart.dto.CartResDto;
import com.business.i4_be.domain.cart.dto.UpdateCartReqDto;
import com.business.i4_be.domain.cart.repository.CartRepository;
import com.business.i4_be.domain.cart.repository.ProductCartRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final ProductCartRepository productCartRepository;

  public CartResDto addCart(Long userId, AddCartReqDto requestDto) {
    return null;
  }

  public CartResDto getCart(Long userId, UUID cartId) {
    return null;
  }

  public CartResDto updateCart(Long userId, UUID cartId, UpdateCartReqDto requestDto) {
    return null;
  }

  public Void deleteCart(Long userId, UUID cartId) {
    return null;
  }
}
