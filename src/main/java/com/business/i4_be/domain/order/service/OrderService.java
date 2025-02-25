package com.business.i4_be.domain.order.service;

import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.order.dto.request.OrderReqDto;
import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.entity.OrderProduct;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.address.repository.AddressRepository;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;

    // 유저 주문 조회(여러건)
    public List<OrderResDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findAll()
                .stream()
                .filter(o -> o.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
        return orders.stream().map(OrderResDto::from).collect(Collectors.toList());
    }

    // 유저 주문 상세 조회(단건)
    public OrderResDto getUserOrderDetail(Long userId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_BELONG_TO_USER);
        }
        return OrderResDto.from(order);
    }

    // 주문 생성
    @Transactional
    public OrderResDto createOrder(Long userId, OrderReqDto request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Order order = Order.builder()
                .user(user)
                .store(store)
                .orderStatus(OrderStatus.COMPLETED)
                .address(address)
                .build();

        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(req -> OrderProduct.builder()
                        .productName(req.getProductName())
                        .price(req.getPrice())
                        .quantity(req.getQuantity())
                        .build())
                .collect(Collectors.toList());

        order.setOrderProducts(orderProducts);

        Order savedOrder = orderRepository.save(order);
        return OrderResDto.from(savedOrder);
    }

    // 주문 취소
    @Transactional
    public OrderResDto cancelOrder(Long userId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_BELONG_TO_USER);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return OrderResDto.from(updatedOrder);
    }
}