package com.business.i4_be.domain.order.service;

import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.order.constant.OrderType;
import com.business.i4_be.domain.order.dto.request.OrderReqDto;
import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.address.repository.AddressRepository;
import com.business.i4_be.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderService orderService;

    private User testUser;
    private Order testOrder;
    private Address testAddress;
    private UUID orderId;
    private UUID addressId;
    private Long userId;

    @BeforeEach
    void setUp() {
        // 테스트 유저 생성
        String uniqueEmail = "test" + UUID.randomUUID().toString() + "@test.com";
        testUser = User.builder()
                .username("testUser")
                .nickname("테스트유저")
                .password("password123")
                .email(uniqueEmail)
                .phoneNumber("01012345678")
                .role(UserRole.USER)
                .build();
        testUser = userRepository.save(testUser);
        userId = testUser.getUserId();

        // 테스트 주소 생성
        testAddress = Address.builder()
                .user(testUser)
                .address("서울시 강남구")
                .build();
        testAddress = addressRepository.save(testAddress);
        addressId = testAddress.getAddressId();

        // 테스트 주문 생성
        testOrder = Order.builder()
                .user(testUser)
                .orderStatus(OrderStatus.COMPLETED)
                .address(testAddress)
                .build();
        testOrder = orderRepository.save(testOrder);
        orderId = testOrder.getOrderId();
    }

    @Test
    @DisplayName("유저의 모든 주문 조회")
    void getUserOrders() {
        // when
        List<OrderResDto> result = orderService.getUserOrders(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("유저의 특정 주문을 상세 조회")
    void getUserOrderDetail() {
        // when
        OrderResDto result = orderService.getUserOrderDetail(userId, orderId);

        // then
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("주문 생성")
    @Rollback(value = false)
    void createOrder() {
        // given
        OrderReqDto request = OrderReqDto.builder()
                .addressId(addressId)
                .orderType(OrderType.DELIVERY)
                .OrderProducts(List.of(
                        new OrderReqDto.OrderProductReq(UUID.randomUUID(),"사탕",1000, 2),
                        new OrderReqDto.OrderProductReq(UUID.randomUUID(),"당근",100, 1),
                        new OrderReqDto.OrderProductReq(UUID.randomUUID(), "호박",500,3)
                ))
                .build();

        // when
        OrderResDto result = orderService.createOrder(userId, request);

        // then
        assertThat(result.getOrderId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED.name());
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        // when
        OrderResDto result = orderService.cancelOrder(userId, orderId);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED.name());
        Order cancelledOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(cancelledOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 조회할 경우 예외가 발생")
    void getUserOrderDetail_OrderNotFound() {
        // given
        UUID nonExistentOrderId = UUID.randomUUID();

        // then
        assertThatThrownBy(() -> orderService.getUserOrderDetail(userId, nonExistentOrderId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");
    }

    @Test
    @DisplayName("다른 유저의 주문을 조회할 경우 예외가 발생")
    void getUserOrderDetail_OrderNotBelongToUser() {
        // given
        final User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("다른유저")
                .password("password123")
                .email("other@test.com")
                .phoneNumber("01087654321")
                .role(UserRole.USER)
                .build());

        // then
        assertThatThrownBy(() -> orderService.getUserOrderDetail(otherUser.getUserId(), orderId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order does not belong to user");
    }
}
