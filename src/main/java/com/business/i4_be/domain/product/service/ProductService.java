package com.business.i4_be.domain.product.service;

import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.dto.ProductResDto;
import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public AddProductResDto addProduct(AddProductReqDto requestDto) {
    return null;
  }

  public UpdateProductResDto updateProduct(UUID productId, UpdateProductReqDto requestDto) {
    return null;
  }

  public Void deleteProduct(UUID storeId, UUID productId) {
    return null;
  }

  public ProductResDto getProduct(UUID storeId, UUID productId) {
    return null;
  }

  public ProductsResDto searchProducts(UUID storeId, String keyword) {
    return null;
  }

  public ProductsResDto getProducts(UUID storeId) {
    return null;
  }
}
