package com.business.i4_be.domain.payment.entity;

import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.payment.constant.PaymentStatus;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "p_payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Long paymentPrice;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
