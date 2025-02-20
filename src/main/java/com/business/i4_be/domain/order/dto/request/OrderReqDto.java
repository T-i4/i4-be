package com.business.i4_be.domain.order.dto.request;

import com.business.i4_be.domain.order.constant.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@Builder
public class OrderReqDto {
    private List<OrderProductReq> OrderProducts;
    private UUID addressId;
    private OrderType orderType;

    //추가
    private UUID storeId;

    @Getter @Setter
    @AllArgsConstructor
    public static class OrderProductReq {
        private UUID productId;
        private String productName;
        private int price;
        private int quantity;
    }
}
