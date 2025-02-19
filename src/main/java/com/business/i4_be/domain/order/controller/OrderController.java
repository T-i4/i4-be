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

    @GetMapping
    public ResponseEntity<List<OrderResDto>> getUserOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResDto> getUserOrderDetail(
            @PathVariable UUID userId,
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.getUserOrderDetail(userId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResDto> createOrder(
            @PathVariable UUID userId,
            @RequestBody OrderReqDto request
    ) {
        return ResponseEntity.ok(orderService.createOrder(userId, request));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResDto> cancelOrder(
            @PathVariable UUID userId,
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }
}