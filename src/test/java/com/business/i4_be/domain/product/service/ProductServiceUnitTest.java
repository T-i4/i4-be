package com.business.i4_be.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductReqDto.ProductDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.global.exception.CustomException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

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
    Product product = makeProduct(reqDto);

    given(productRepository.existsByProductName(reqDto.getProductDto().getProductName()))
        .willReturn(Boolean.FALSE);

    given(productRepository.save(any(Product.class))).willReturn(product);

    // when
    AddProductResDto resDto = productService.addProduct(userId, reqDto);

    // then
    assertThat(resDto).isNotNull();
  }

  @Test
  @DisplayName("상품 등록 실패 - 중복된 상품명")
  void product_save_fail() {

    // given
    Long userId = 1L;

    AddProductReqDto reqDto = makeAddProductReqDto("치즈 떡볶이", 2, 5500);
    given(productRepository.existsByProductName(reqDto.getProductDto().getProductName()))
        .willReturn(Boolean.TRUE);

    // when, then
    assertThrows(CustomException.class, () ->
        productService.addProduct(userId, reqDto));
  }

  private Product makeProduct(AddProductReqDto reqDto) {
    return Product.builder()
        .productId(UUID.fromString("619cfbcd-f25b-4a29-b973-154ee1736da7"))
        .productName(reqDto.getProductDto().getProductName())
        .quantity(reqDto.getProductDto().getQuantity())
        .price(reqDto.getProductDto().getPrice())
        .text(reqDto.getProductDto().getText())
        .status(reqDto.getProductDto().getStatus())
        .image(reqDto.getProductDto().getImageUrl())
        .build();
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