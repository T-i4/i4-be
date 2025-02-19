package com.business.i4_be.domain.payment.service;

import com.business.i4_be.domain.payment.dto.request.PaymentReqDto;
import com.business.i4_be.domain.payment.dto.response.PaymentResDto;
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

    // 전체 결제목록 조회
    public List<PaymentResDto> getAllPayments() {
        return null;
    }

    // 유저의 결제 목록 조회 (여러건)
    public List<PaymentResDto> getUserPayments(Long userId) {
        return null;
    }

    // 유저의 특정 결제 기록 조회 (단건)
    public PaymentResDto getUserPaymentDetail(Long userId, UUID paymentId) {
        return null;
    }

    // 유저의 결제 기록 생성
    @Transactional
    public PaymentResDto createPayment(Long userId, PaymentReqDto request) {
        return null;
    }

    // 결제 취소
    @Transactional
    public PaymentResDto cancelPayment(Long userId, UUID paymentId) {
        return null;
    }
}
