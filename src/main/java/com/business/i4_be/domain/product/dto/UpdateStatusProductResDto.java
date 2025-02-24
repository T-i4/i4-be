package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.entity.Product;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusProductResDto {

  private List<ProductDto> products;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductDto {

    private UUID productId;
    private ProductStatus status;

    public static ProductDto from(Product product) {
      return ProductDto.builder()
          .productId(product.getProductId())
          .status(product.getStatus())
          .build();
    }
  }

  public static UpdateStatusProductResDto from(List<Product> products) {
    List<ProductDto> productList = products.stream()
        .map(ProductDto::from).toList();

    return UpdateStatusProductResDto.builder()
        .products(productList)
        .build();
  }
}
