package com.business.i4_be.domain.payment.dto.response;

import com.business.i4_be.domain.payment.constant.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PaymentResDto {
    private List<PaymentDetail> payments;

    @Data
    public static class PaymentDetail {
        private UUID paymentId;
        private Long userId;
        private Long amount;
        private PaymentStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
