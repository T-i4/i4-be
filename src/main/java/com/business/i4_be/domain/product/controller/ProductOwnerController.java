package com.business.i4_be.domain.product.controller;

import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.dto.UpdateStatusProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateStatusProductResDto;
import com.business.i4_be.domain.product.service.ProductService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner/v1/products")
@RequiredArgsConstructor
public class ProductOwnerController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ProductsResDto> getProducts(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam("storeId") UUID storeId) {
    return ResponseEntity.ok().body(
        productService.getProducts(userDetails.getUser().getId(),storeId));
  }

  @PatchMapping
  public ResponseEntity<UpdateStatusProductResDto> updateStatusProducts(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam("storeId") UUID storeId,
      @RequestBody UpdateStatusProductReqDto reqDto
  ) {
    return ResponseEntity.ok().body(productService.updateStatusProduct(
        userDetails.getUser().getId(), storeId, reqDto));
  }
}
