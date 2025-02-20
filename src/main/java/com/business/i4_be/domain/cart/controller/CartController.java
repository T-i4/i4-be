package com.business.i4_be.domain.cart.controller;

import com.business.i4_be.domain.cart.dto.AddCartReqDto;
import com.business.i4_be.domain.cart.dto.CartResDto;
import com.business.i4_be.domain.cart.dto.UpdateCartReqDto;
import com.business.i4_be.domain.cart.service.CartService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<CartResDto> addCart(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody AddCartReqDto requestDto) {
    return ResponseEntity.ok(cartService.addCart(userDetails.getUser().getId(), requestDto));
  }

  @GetMapping("/{cartId}")
  public ResponseEntity<CartResDto> getCart(
      @PathVariable("cartId") UUID cartId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return ResponseEntity.ok(cartService.getCart(userDetails.getUser().getId(), cartId));
  }

  @PutMapping("/{cartId}")
  public ResponseEntity<CartResDto> updateCart(
      @PathVariable("cartId") UUID cartId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UpdateCartReqDto requestDto
  ) {
    return ResponseEntity.ok(cartService.updateCart(userDetails.getUser().getId(), cartId, requestDto));
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<Void> deleteCart(
      @PathVariable("cartId") UUID cartId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    cartService.deleteCart(userDetails.getUser().getId(), cartId);
    return ResponseEntity.ok().build();
  }
}
