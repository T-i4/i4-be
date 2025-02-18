package com.business.i4_be.domain.order.entity;

import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.user.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "order_request")
    private String orderRequest;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private String oderType;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",nullable = false)
    private Address address;


}
