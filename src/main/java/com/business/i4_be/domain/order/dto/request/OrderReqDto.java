package com.business.i4_be.domain.order.dto.request;

import com.business.i4_be.domain.order.constant.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class OrderReqDto {
    private List<OrderItemRequest> items;
    private UUID addressId;
    private OrderType orderType;

    @Getter @Setter
    public static class OrderItemRequest {
        private UUID productId;
        private int quantity;
    }
}