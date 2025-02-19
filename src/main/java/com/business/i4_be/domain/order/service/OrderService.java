package com.business.i4_be.domain.order.service;

import com.business.i4_be.domain.order.dto.request.OrderReqDto;
import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResDto> getUserOrders(UUID userId) {
        return null;
    }

    public OrderResDto getUserOrderDetail(UUID userId, UUID orderId) {
        return null;
    }

    @Transactional
    public OrderResDto createOrder(UUID userId, OrderReqDto request) {
        return null;
    }

    @Transactional
    public OrderResDto cancelOrder(UUID userId, UUID orderId) {
        return null;
    }
}