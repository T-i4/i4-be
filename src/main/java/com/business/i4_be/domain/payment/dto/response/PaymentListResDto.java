package com.business.i4_be.domain.payment.dto.response;

import com.business.i4_be.domain.payment.constant.PaymentStatus;
import com.business.i4_be.domain.payment.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentListResDto {
    private Long userId;
    private List<PaymentDetail> payments;

    public static PaymentListResDto of(Long userId, List<Payment> payment) {
        return PaymentListResDto.builder()
                .userId(userId)
                .payments(payment.stream().map(PaymentDetail::from).collect(Collectors.toList()))
                .build();
    }

    @Getter @Setter
    @Builder
    public static class PaymentDetail {
        private UUID paymentId;
        private Long amount;
        private PaymentStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PaymentDetail from(Payment payment) {
            return PaymentDetail.builder()
                    .paymentId(payment.getPaymentId())
                    .amount(payment.getPaymentPrice())
                    .status(payment.getPaymentStatus())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        }
    }

}
