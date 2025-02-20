package com.business.i4_be.domain.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.Getter;

@Getter
public class AddCartReqDto {
  @NotNull
  private UUID storeId;

  @Valid
  private Product product;

  @Getter
  public static class Product {
    @NotNull
    private UUID productId;

    @Positive(message = "수량은 1개 이상 선택해야 합니다.")
    private Integer quantity;
  }
}
