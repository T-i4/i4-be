package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.entity.Product;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResDto {

  private UUID storeId;
  private ProductDto productDto;

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  private static class ProductDto {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer price;
    private String text;
    private ProductStatus status;
    private String imageUrl;

  }

  public static ProductResDto from(UUID storeId, Product product) {
    ProductDto productDto = ProductDto.builder()
          .productId(product.getProductId())
          .productName(product.getProductName())
          .quantity(product.getQuantity())
          .price(product.getPrice())
          .text(product.getText())
          .status(product.getStatus())
          .imageUrl(product.getImage())
          .build();

    return ProductResDto.builder()
        .storeId(storeId)
        .productDto(productDto)
        .build();
  }
}
