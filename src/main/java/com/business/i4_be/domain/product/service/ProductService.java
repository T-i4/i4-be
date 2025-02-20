package com.business.i4_be.domain.product.service;

import static com.business.i4_be.global.exception.ErrorCode.ALREADY_EXIST_PRODUCT;

import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.dto.ProductResDto;
import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public AddProductResDto addProduct(Long userId, AddProductReqDto requestDto)
      throws CustomException {

    // TODO User, Store 검증 필요

    boolean exist = productRepository.existsByProductName(
        requestDto.getProductDto().getProductName());
    if (exist) {
      throw new CustomException(ALREADY_EXIST_PRODUCT);
    }

    return AddProductResDto.from(
        productRepository.save(AddProductReqDto.toEntity(requestDto.getProductDto())));
  }

  public UpdateProductResDto updateProduct(UUID productId, UpdateProductReqDto requestDto) {
    return null;
  }

  public Void deleteProduct(UUID storeId, UUID productId) {
    return null;
  }

  @Transactional(readOnly = true)
  public ProductResDto getProduct(UUID storeId, UUID productId) {

    // TODO store, user 검증 필요
    Product product = productRepository.findByProductIdAndStore_storeId(productId, storeId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ProductResDto.from(storeId, product);
  }

  @Transactional(readOnly = true)
  public ProductsResDto searchProducts(UUID storeId, String keyword) {
    // TODO user store 검증 필요
    List<Product> products = productRepository.findByStore_storeIdAndProductNameContaining(storeId, keyword);
    return ProductsResDto.from(storeId, products);
  }

  @Transactional(readOnly = true)
  public ProductsResDto getProducts(UUID storeId) {
    // TODO user, storeId 검증 필요
    List<Product> products = productRepository.findByStore_storeId(storeId);
    return ProductsResDto.from(storeId, products);
  }
}
