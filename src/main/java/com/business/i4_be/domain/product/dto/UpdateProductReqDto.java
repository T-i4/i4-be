package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UpdateProductReqDto {

  @NotNull
  private UUID storeId;

  @Valid
  private Product product;

  @Getter
  public static class Product {

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
}
