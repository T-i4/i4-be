package com.business.i4_be.domain.payment.service;

import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.payment.constant.PaymentStatus;
import com.business.i4_be.domain.payment.dto.request.PaymentReqDto;
import com.business.i4_be.domain.payment.dto.response.PaymentListResDto;
import com.business.i4_be.domain.payment.dto.response.PaymentResDto;
import com.business.i4_be.domain.payment.entity.Payment;
import com.business.i4_be.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // 전체 결제목록 조회
    public PaymentListResDto getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return PaymentListResDto.of(null, payments);
    }

    // 유저의 결제 목록 조회 (여러건)
    public PaymentListResDto getUserPayments(Long userId) {
        //userId로 order를 조회해서 payment를 조회
        List<Payment> payments = paymentRepository.findAllByOrderUserId(userId);
        return PaymentListResDto.of(userId, payments);
    }

    // 유저의 특정 결제 기록 조회 (단건)
    public PaymentResDto getUserPaymentDetail(Long userId, UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다."));
        return PaymentResDto.of(userId, payment);
    }

    // 유저의 결제 기록 생성
    @Transactional
    public PaymentResDto createPayment(Long userId, PaymentReqDto request) {
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));

        Payment payment = Payment.builder()
            .paymentStatus(PaymentStatus.COMPLETED)
            .order(order)
            .paymentPrice(request.getAmount())
            .build();

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResDto.of(userId, savedPayment);
    }

    // 결제 취소
    @Transactional
    public PaymentResDto cancelPayment(Long userId, UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다."));

        // payment의 상태를 취소로 변경
        payment.setPaymentStatus(PaymentStatus.CANCELLED);

        return PaymentResDto.of(userId, payment);
    }
}
