package com.business.i4_be.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderResDto {
    private UUID orderId;
    private UUID userId;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDto> items;

    @Getter
    @Builder
    public static class OrderItemDto {
        private String productId;
        private int quantity;
        private double price;
    }

}