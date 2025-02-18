package com.business.i4_be.domain.cart.controller;

import com.business.i4_be.domain.cart.dto.AddCartReqDto;
import com.business.i4_be.domain.cart.dto.CartResDto;
import com.business.i4_be.domain.cart.dto.UpdateCartReqDto;
import com.business.i4_be.domain.cart.service.CartService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<CartResDto> addCart(
      @RequestParam("userId") UUID userId, // 추후 변경
      @Valid @RequestBody AddCartReqDto requestDto) {
    return ResponseEntity.ok(cartService.addCart(userId, requestDto));
  }

  @GetMapping("/{cartId}")
  public ResponseEntity<CartResDto> getCart(
      @PathVariable("cartId") UUID cartId,
      @RequestParam("userId") UUID userId
  ) {
    return ResponseEntity.ok(cartService.getCart(userId, cartId));
  }

  @PutMapping("/{cartId}")
  public ResponseEntity<CartResDto> updateCart(
      @PathVariable("cartId") UUID cartId,
      @RequestParam("userId") UUID userId,
      @Valid @RequestBody UpdateCartReqDto requestDto
  ) {
    return ResponseEntity.ok(cartService.updateCart(userId, cartId, requestDto));
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<Void> deleteCart(
      @PathVariable("cartId") UUID cartId,
      @RequestParam("userId") UUID userId
  ) {
    return ResponseEntity.ok(cartService.deleteCart(userId, cartId));
  }
}
