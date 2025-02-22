package com.business.i4_be.domain.product.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.dto.ProductResDto;
import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.service.ProductService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<AddProductResDto> addProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody AddProductReqDto requestDto) {
    return ResponseEntity.status(CREATED).
        body(productService.addProduct(userDetails.getUser().getId(), requestDto));
  }

  @PutMapping("/{productId}")
  public ResponseEntity<UpdateProductResDto> updateProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("productId") UUID productId,
      @Valid @RequestBody UpdateProductReqDto requestDto
  ) {
    return ResponseEntity.ok().body(
        productService.updateProduct(userDetails.getUser().getId(), productId, requestDto));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("productId") UUID productId,
      @RequestParam("storeId") UUID storeId
  ) {
    productService.deleteProduct(userDetails.getUser().getId(), storeId, productId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductResDto> getProduct(
      @PathVariable("productId") UUID productId,
      @RequestParam("storeId") UUID storeId
  ) {
    return ResponseEntity.ok().body(productService.getProduct(storeId, productId));
  }

  @GetMapping
  public ResponseEntity<ProductsResDto> searchProducts(
      @RequestParam("storeId") UUID storeId,
      @RequestParam("keyword") String keyword
  ) {
    return ResponseEntity.ok().body(productService.searchProducts(storeId, keyword));
  }
}
