package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductReqDto {

  @NotNull
  private UUID storeId;

  @Valid
  private ProductDto productDto;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductDto {

    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @PositiveOrZero(message = "0이상의 값을 입력해야 합니다.")
    private Integer quantity;

    @NotNull(message = "가격은 최소 100원 이상이어야 합니다.")
    @Min(value = 100, message = "가격은 최소 100원 이상이어야 합니다.")
    private Integer price;

    private String text;

    @NotNull
    private ProductStatus status;

    private String imageUrl;
  }

  public static Product toEntity(ProductDto productDto) {
    return Product.builder()
        .productName(productDto.getProductName())
        .quantity(productDto.getQuantity())
        .price(productDto.getPrice())
        .text(productDto.getText())
        .status(productDto.getStatus())
        .image(productDto.getImageUrl())
        .build();
  }
}

