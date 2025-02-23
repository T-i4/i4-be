package com.business.i4_be.domain.product.service;

import static com.business.i4_be.global.exception.ErrorCode.ALREADY_EXIST_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductReqDto.ProductDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.global.exception.CustomException;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

  @Mock
  StoreRepository storeRepository;

  @Mock
  ProductRepository productRepository;

  @InjectMocks
  ProductService productService;

  @Test
  @DisplayName("상품 등록 성공")
  void product_save() {
    // given
    Long userId = 1L;

    AddProductReqDto reqDto = makeAddProductReqDto("치즈 떡볶이", 2, 5500);
    Product product = makeAddProduct(reqDto);
    Store store = makeStore(reqDto.getStoreId());

    given(storeRepository.findById(reqDto.getStoreId())).willReturn(Optional.ofNullable(store));

    given(productRepository.existsByProductName(reqDto.getProductDto().getProductName()))
        .willReturn(Boolean.FALSE);

    given(productRepository.save(any(Product.class))).willReturn(product);

    // when
    AddProductResDto resDto = productService.addProduct(userId, reqDto);

    // then
    assertThat(reqDto.getProductDto().getProductName()).isEqualTo(
        resDto.getProductDto().getProductName());
    assertThat(reqDto.getProductDto().getQuantity()).isEqualTo(
        resDto.getProductDto().getQuantity());
    assertThat(reqDto.getProductDto().getPrice()).isEqualTo(resDto.getProductDto().getPrice());
    assertThat(reqDto.getProductDto().getText()).isEqualTo(resDto.getProductDto().getText());
    assertThat(reqDto.getProductDto().getStatus()).isEqualTo(resDto.getProductDto().getStatus());
    assertThat(reqDto.getProductDto().getImageUrl()).isEqualTo(
        resDto.getProductDto().getImageUrl());
  }

  @Test
  @DisplayName("상품 등록 실패 - 중복된 상품명")
  void product_save_fail() {
    // given
    Long userId = 1L;

    AddProductReqDto reqDto = makeAddProductReqDto("치즈 떡볶이", 2, 5500);

    Store store = makeStore(reqDto.getStoreId());
    given(storeRepository.findById(reqDto.getStoreId())).willReturn(Optional.ofNullable(store));

    given(productRepository.existsByProductName(reqDto.getProductDto().getProductName()))
        .willReturn(Boolean.TRUE);

    // when, then
    CustomException e = assertThrows(CustomException.class, () ->
        productService.addProduct(userId, reqDto));
    assertEquals(ALREADY_EXIST_PRODUCT.getMessage(), e.getMessage());
  }

  @Test
  @DisplayName("상품 수정 성공")
  void product_update() {
    //given
    Long userId = 1L;
    UpdateProductReqDto reqDto = makeUpdateProductReqDto("치즈 떡볶이", 4, 5500);
    Product product = makeUpdateProduct(reqDto);
    given(productRepository.findByProductIdAndStore_storeId(product.getProductId(),
        reqDto.getStoreId()))
        .willReturn(Optional.of(product));

    given(productRepository.existsByProductName(any()))
        .willReturn(Boolean.FALSE);

    UpdateProductReqDto changeDto = makeUpdateProductReqDto("마라 떡볶이", 4, 2000);

    //when
    UpdateProductResDto resDto = productService.updateProduct(userId, product.getProductId(),
        changeDto);

    //then
    assertThat(changeDto.getProductDto().getProductName()).isEqualTo(
        resDto.getProductDto().getProductName());
    assertThat(changeDto.getProductDto().getQuantity()).isEqualTo(
        resDto.getProductDto().getQuantity());
    assertThat(changeDto.getProductDto().getPrice()).isEqualTo(resDto.getProductDto().getPrice());
    assertThat(changeDto.getProductDto().getText()).isEqualTo(resDto.getProductDto().getText());
    assertThat(changeDto.getProductDto().getStatus()).isEqualTo(resDto.getProductDto().getStatus());
    assertThat(changeDto.getProductDto().getImageUrl()).isEqualTo(
        resDto.getProductDto().getImageUrl());
  }

  @Test
  @DisplayName("상품 수정 실패 - 변경하는 상품명이 이미 존재")
  void product_update_fail() {
    Long userId = 1L;
    UpdateProductReqDto reqDto = makeUpdateProductReqDto("치즈 떡볶이", 4, 5500);
    Product product = makeUpdateProduct(reqDto);
    given(productRepository.findByProductIdAndStore_storeId(product.getProductId(),
        reqDto.getStoreId()))
        .willReturn(Optional.of(product));

    given(productRepository.existsByProductName(any()))
        .willReturn(Boolean.TRUE);

    UpdateProductReqDto changeDto = makeUpdateProductReqDto("마라 떡볶이", 4, 5500);

    // when, then
    CustomException e = assertThrows(CustomException.class, () ->
        productService.updateProduct(userId, product.getProductId(), changeDto));
    assertEquals(ALREADY_EXIST_PRODUCT.getMessage(), e.getMessage());
  }

  private Store makeStore(UUID storeId) {
    return Store.builder()
        .storeId(storeId)
        .storeName("신전 떡볶이")
        .storeAddress("Test 주소")
        .storeDetail("떡볶이 파는 곳")
        .storeNumber("010-2334-3442")
        .category(StoreCategory.분식)
        .openTime(LocalTime.now())
        .closedTime(LocalTime.now())
        .isOpen(StoreIsOpen.OPEN)
        .build();
  }

  private Product makeAddProduct(AddProductReqDto reqDto) {
    return Product.builder()
        .productId(UUID.randomUUID())
        .productName(reqDto.getProductDto().getProductName())
        .quantity(reqDto.getProductDto().getQuantity())
        .price(reqDto.getProductDto().getPrice())
        .text(reqDto.getProductDto().getText())
        .status(reqDto.getProductDto().getStatus())
        .image(reqDto.getProductDto().getImageUrl())
        .build();
  }

  private AddProductReqDto makeAddProductReqDto(String productName, Integer quantity,
      Integer price) {
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

  private Product makeUpdateProduct(UpdateProductReqDto reqDto) {
    return Product.builder()
        .productId(UUID.randomUUID())
        .productName(reqDto.getProductDto().getProductName())
        .quantity(reqDto.getProductDto().getQuantity())
        .price(reqDto.getProductDto().getPrice())
        .text(reqDto.getProductDto().getText())
        .status(reqDto.getProductDto().getStatus())
        .image(reqDto.getProductDto().getImageUrl())
        .build();
  }

  private UpdateProductReqDto makeUpdateProductReqDto(String productName, Integer quantity,
      Integer price) {
    UpdateProductReqDto.ProductDto productDto = UpdateProductReqDto.ProductDto.builder()
        .productName(productName)
        .quantity(quantity)
        .price(price)
        .text("TEST")
        .status(ProductStatus.SALE)
        .imageUrl("TESTURL")
        .build();

    return UpdateProductReqDto.builder()
        .storeId(UUID.fromString("d730d003-2956-47a6-9041-4c100dd1833e"))
        .productDto(productDto)
        .build();
  }
}