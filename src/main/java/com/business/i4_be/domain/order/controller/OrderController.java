package com.business.i4_be.domain.order.controller;

import com.business.i4_be.domain.order.dto.request.OrderReqDto;
import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 유저 주문 조회(여러건)
    @GetMapping
    public ResponseEntity<List<OrderResDto>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    // 유저 주문 상세 조회(단건)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResDto> getUserOrderDetail(
            @PathVariable Long userId,
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.getUserOrderDetail(userId, orderId));
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResDto> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderReqDto request
    ) {
        return ResponseEntity.ok(orderService.createOrder(userId, request));
    }

    // 주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResDto> cancelOrder(
            @PathVariable Long userId,
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }
}