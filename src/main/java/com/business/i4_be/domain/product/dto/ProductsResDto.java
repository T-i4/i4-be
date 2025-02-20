package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.entity.Product;
import java.util.List;
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
public class ProductsResDto {

  private UUID storeId;
  private List<ProductDto> products;

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

    public static ProductDto from(Product product) {
      return ProductDto.builder()
          .productId(product.getProductId())
          .productName(product.getProductName())
          .quantity(product.getQuantity())
          .price(product.getPrice())
          .text(product.getText())
          .status(product.getStatus())
          .imageUrl(product.getImage())
          .build();
    }
  }

  public static ProductsResDto from(UUID storeId, List<Product> products) {
    List<ProductDto> productList = products.stream()
        .map(ProductDto::from).toList();

    return ProductsResDto.builder()
        .storeId(storeId)
        .products(productList)
        .build();
  }
}
