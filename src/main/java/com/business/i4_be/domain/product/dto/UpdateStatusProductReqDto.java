package com.business.i4_be.domain.product.dto;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.global.annotation.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class UpdateStatusProductReqDto {

  @Valid
  private List<ProductDto> products;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductDto {

    @NotNull(message = "상품 정보가 필요합니다.")
    private UUID productId;

    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus status;
  }
}
