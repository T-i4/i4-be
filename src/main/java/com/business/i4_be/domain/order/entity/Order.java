package com.business.i4_be.domain.order.entity;

import com.business.i4_be.domain.order.constant.OrderStatus;
import com.business.i4_be.domain.order.constant.OrderType;
import com.business.i4_be.domain.review.entity.Review;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.address.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private OrderType oderType;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",nullable = false)
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;








    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review;


    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts.clear();
        if (orderProducts != null) {
            orderProducts.forEach(this::addOrderProduct);
        }
    }
}
