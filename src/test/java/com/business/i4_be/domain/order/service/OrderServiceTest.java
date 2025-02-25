package com.business.i4_be.domain.order.service;

import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.address.repository.AddressRepository;
import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.order.constant.OrderType;
import com.business.i4_be.domain.order.dto.request.OrderReqDto;
import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
    private StoreRepository storeRepository;

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
        String uniquePhoneNumber = "010" + UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 8);
        String uniqueUsername = "testUser" + UUID.randomUUID().toString().substring(0, 8);

        testUser = User.builder()
                .username(uniqueUsername)
                .nickname("테스트유저")
                .password("password123")
                .email(uniqueEmail)
                .phoneNumber(uniquePhoneNumber)
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
        // 테스트용 Store 생성 및 저장
        Store store = Store.builder()
                .storeName("테스트 상점")
                .storeAddress("서울시 강남구")
                .storeDetail("테스트 상점 상세 정보")
                .openTime(LocalTime.of(9, 0))
                .closedTime(LocalTime.of(22, 0))
                .storeNumber("02-1234-5678")
                .category(StoreCategory.한식)
                .isOpen(StoreIsOpen.OPEN)
                .user(testUser)
                .build();
        store = storeRepository.save(store);

        // Store ID 확인
        System.out.println("Store ID: " + store.getStoreId());
        assertThat(store.getStoreId()).isNotNull();  // ID 생성 확인


        OrderReqDto request = OrderReqDto.builder()
                .addressId(addressId)
                .orderType(OrderType.DELIVERY)
                .OrderProducts(List.of(
                        new OrderReqDto.OrderProductReq(store.getStoreId(),"사탕",1000, 2),
                        new OrderReqDto.OrderProductReq(store.getStoreId(),"당근",100, 1),
                        new OrderReqDto.OrderProductReq(store.getStoreId(), "호박",500,3)
                ))
                .storeId(store.getStoreId())
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
}
