package com.business.i4_be.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductReqDto.ProductDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.global.exception.CustomException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ProductServiceTest {

  @Autowired
  ProductService productService;

  @Autowired
  ProductRepository productRepository;

  @Test
  @DisplayName("상품 등록 성공")
  @Transactional
  void product_save() {
    // given
    Long userId = 1L;

    AddProductReqDto reqDto = makeAddProductReqDto("치즈 떡볶이", 2, 5500);

    // when
    productService.addProduct(userId, reqDto);

    //then
    Optional<Product> addProduct = productRepository.findByProductName(reqDto.getProductDto().getProductName());

    assertThat(addProduct.isPresent()).isTrue();
    assertThat(addProduct.get().getProductName()).isEqualTo(reqDto.getProductDto().getProductName());
    assertThat(addProduct.get().getQuantity()).isEqualTo(reqDto.getProductDto().getQuantity());
    assertThat(addProduct.get().getPrice()).isEqualTo(reqDto.getProductDto().getPrice());
  }

  @Test
  @DisplayName("상품 등록 실패 - 중복된 상품명")
  @Transactional
  void product_save_fail() {
    // given
    Long userId = 1L;

    AddProductReqDto reqDto = makeAddProductReqDto("치즈 떡볶이", 2, 5500);


    // when
    productService.addProduct(userId, reqDto);

    //then
    Assertions.assertThrows(CustomException.class,
        ()-> productService.addProduct(userId, reqDto));
  }

  private AddProductReqDto makeAddProductReqDto(String productName, Integer quantity, Integer price) {
    ProductDto productDto = ProductDto.builder()
        .productName(productName)
        .quantity(quantity)
        .price(price)
        .text("TEST")
        .status(ProductStatus.SALE)
        .imageUrl("TESTURL")
        .build();

    return AddProductReqDto.builder()
        .storeId(UUID.fromString("d730d003-2956-47a6-9041-4c100dd1833e"))
        .productDto(productDto)
        .build();
  }

}