package com.business.i4_be.domain.product.service;

import static com.business.i4_be.global.exception.ErrorCode.ALREADY_EXIST_PRODUCT;
import static com.business.i4_be.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.business.i4_be.common.DeleteSupport;
import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductReqDto.ProductDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class ProductServiceTest extends DeleteSupport {

  @Autowired
  ProductService productService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  StoreRepository storeRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  User user;
  Store store;

  @BeforeEach
  void setUp() {
    User addUser = makeUser();
    user = userRepository.save(addUser);
    Store addStore = makeStore(user);
    store = storeRepository.save(addStore);
  }

  @Test
  @DisplayName("상품 등록 성공")
  void product_save() {
    // given
    AddProductReqDto reqDto = makeAddProductReqDto(store.getStoreId(), "치즈 떡볶이", 2, 5500);

    // when
    productService.addProduct(user.getId(), reqDto);

    //then
    Optional<Product> addProduct = productRepository.findByProductName(
        reqDto.getProductDto().getProductName());

    assertThat(addProduct.isPresent()).isTrue();
    assertThat(addProduct.get().getProductName()).isEqualTo(
        reqDto.getProductDto().getProductName());
    assertThat(addProduct.get().getQuantity()).isEqualTo(reqDto.getProductDto().getQuantity());
    assertThat(addProduct.get().getPrice()).isEqualTo(reqDto.getProductDto().getPrice());
    assertThat(addProduct.get().getText()).isEqualTo(reqDto.getProductDto().getText());
    assertThat(addProduct.get().getStatus()).isEqualTo(reqDto.getProductDto().getStatus());
    assertThat(addProduct.get().getImage()).isEqualTo(reqDto.getProductDto().getImageUrl());
  }

  @Test
  @DisplayName("상품 등록 실패 - 중복된 상품명")
  void product_save_fail() {
    // given;
    AddProductReqDto reqDto = makeAddProductReqDto(store.getStoreId(), "치즈 떡볶이", 2, 5500);
    Product product = makeAddProduct(reqDto);

    productRepository.save(product);

    // when, then
    assertThrows(CustomException.class,
        () -> productService.addProduct(user.getId(), reqDto));
  }

  @Test
  @DisplayName("상품 수정 성공")
  void product_update() {
    //given
    UpdateProductReqDto reqDto = makeUpdateProductReqDto("치즈 떡볶이", 4, 5500);
    Product product = makeUpdateProduct(reqDto);

    Product addProduct = productRepository.save(product);

    UpdateProductReqDto changeDto = makeUpdateProductReqDto("마라 떡볶이", 6, 3300);

    //when
    UpdateProductResDto resDto = productService.updateProduct(user.getId(), addProduct.getProductId(),
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
    //given
    UpdateProductReqDto reqDto1 = makeUpdateProductReqDto("치즈 떡볶이", 4, 5500);
    Product product1 = makeUpdateProduct(reqDto1);
    Product addProduct1 = productRepository.save(product1);

    UpdateProductReqDto reqDto2 = makeUpdateProductReqDto("마라 떡볶이", 3, 4500);
    Product product2 = makeUpdateProduct(reqDto2);
    Product addProduct2 = productRepository.save(product2);

    UpdateProductReqDto changeDto = makeUpdateProductReqDto("마라 떡볶이", 6, 3300);

    //when
    CustomException e = assertThrows(CustomException.class,
        () -> productService.updateProduct(user.getId(), addProduct1.getProductId(),
            changeDto));
    assertEquals(ALREADY_EXIST_PRODUCT.getMessage(), e.getErrorCode().getMessage());
  }

  private AddProductReqDto makeAddProductReqDto(UUID storeId, String productName, Integer quantity,
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
        .storeId(storeId)
        .productDto(productDto)
        .build();
  }

  @Nested
  @DisplayName("상품 삭제 테스트")
  class DeleteProduct {

    Product product;

    @Test
    @DisplayName("상품 삭제 실패 - 상품 미존재")
    void product_delete_fail() {
      //given
      product = makeProduct("치즈 떡볶이", 4, 5000);
      Product saveProduct = productRepository.save(product);

      //when, then
      CustomException e = assertThrows(CustomException.class,
          () -> productService.deleteProduct(user.getId(), store.getStoreId(), UUID.randomUUID()));
      assertEquals(PRODUCT_NOT_FOUND.getMessage(), e.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("상품 삭제 성공 - 삭제 레코드는 조회 안됨.")
    void product_delete() {
      //given
      product = makeProduct("치즈 떡볶이", 4, 5000);
      Product saveProduct = productRepository.save(product);

      //when
      productService.deleteProduct(user.getId(), store.getStoreId(), saveProduct.getProductId());

      //then
      Optional<Product> checkProduct = productRepository.findByProductIdAndStore_storeId(
          saveProduct.getProductId(), store.getStoreId());
      assertThat(checkProduct.isPresent()).isFalse();
    }
  }

  private Product makeAddProduct(AddProductReqDto reqDto) {
    return Product.builder()
        .productName(reqDto.getProductDto().getProductName())
        .quantity(reqDto.getProductDto().getQuantity())
        .price(reqDto.getProductDto().getPrice())
        .text(reqDto.getProductDto().getText())
        .status(reqDto.getProductDto().getStatus())
        .image(reqDto.getProductDto().getImageUrl())
        .store(store)
        .build();
  }

  private Product makeUpdateProduct(UpdateProductReqDto reqDto) {
    return Product.builder()
        .productName(reqDto.getProductDto().getProductName())
        .quantity(reqDto.getProductDto().getQuantity())
        .price(reqDto.getProductDto().getPrice())
        .text(reqDto.getProductDto().getText())
        .status(reqDto.getProductDto().getStatus())
        .image(reqDto.getProductDto().getImageUrl())
        .store(store)
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
        .storeId(store.getStoreId())
        .productDto(productDto)
        .build();
  }

  private Store makeStore(User user) {
    return Store.builder()
        .storeName("신전 떡볶이")
        .storeAddress("Test 주소")
        .storeDetail("떡볶이 파는 곳")
        .storeNumber("010-2334-3442")
        .category(StoreCategory.분식)
        .openTime(LocalTime.now())
        .closedTime(LocalTime.now())
        .isOpen(StoreIsOpen.OPEN)
        .user(user)
        .build();
  }

  private User makeUser() {
    return User.builder()
        .username("testuser1")
        .nickname("test1")
        .password(passwordEncoder.encode("Password123!"))
        .email("test70@naver.com")
        .phoneNumber("01029560340")
        .role(UserRole.MASTER)
        .build();
  }

  private Product makeProduct(String productName, Integer quantity, Integer price) {
    return Product.builder()
        .productName(productName)
        .quantity(quantity)
        .price(price)
        .text("TEST")
        .status(ProductStatus.SALE)
        .image("TESTURL")
        .store(store)
        .build();
  }
}