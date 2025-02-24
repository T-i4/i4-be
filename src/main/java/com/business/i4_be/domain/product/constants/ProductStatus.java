package com.business.i4_be.domain.product.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductStatus {
  HIDDEN,
  SOLD_OUT,
  SALE;

  @JsonCreator
  public static ProductStatus from(String value) {
    for (ProductStatus status : ProductStatus.values()) {
      if (status.name().equalsIgnoreCase(value)) { // name()을 이용해 Enum 이름과 비교
        return status;
      }
    }
    return null;
  }
}
