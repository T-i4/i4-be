package com.business.i4_be.domain.order.dto.response;

import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.entity.OrderProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
public class OrderResDto {
    private UUID orderId;

    private Long userId;
    private String orderStatus;
    private String orderType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderProductDto> orderProduct;

    public static OrderResDto from(Order order) {
        return OrderResDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .orderStatus(order.getOrderStatus().name())
                .orderType(order.getOrderType().name())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderProduct(order.getOrderProducts().stream()
                        .map(OrderProductDto::from)
                        .collect(Collectors.toList()))  // 주문 아이템 매핑 필요시 추가
                .build();
    }

    @Getter
    @Builder
    public static class OrderProductDto {
        private UUID productId;
        private String productName;
        private int price;
        private int quantity;

        public static OrderProductDto from(OrderProduct orderProduct) {
            return OrderProductDto.builder()
                    .productId(orderProduct.getOrderProductId())
                    .productName(orderProduct.getProductName())
                    .price(orderProduct.getPrice())
                    .quantity(orderProduct.getQuantity())
                    .build();
        }
    }
}