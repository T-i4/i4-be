package com.business.i4_be.domain.payment.dto.response;

import com.business.i4_be.domain.payment.constant.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class PaymentResDto {
    private List<PaymentDetail> payments;

    @Getter
    public static class PaymentDetail {
        private UUID paymentId;
        private Long userId;
        private Long amount;
        private PaymentStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
