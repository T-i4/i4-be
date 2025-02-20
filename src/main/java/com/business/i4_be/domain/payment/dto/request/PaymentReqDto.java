package com.business.i4_be.domain.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class PaymentReqDto {
    private UUID orderId;
    private Long amount;
}
