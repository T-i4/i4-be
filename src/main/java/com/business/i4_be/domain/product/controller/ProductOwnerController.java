package com.business.i4_be.domain.product.controller;

import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
      @RequestParam("userId") Long userId,
      @RequestParam("storeId") UUID storeId) {
    return ResponseEntity.ok().body(productService.getProducts(storeId));
  }
}
