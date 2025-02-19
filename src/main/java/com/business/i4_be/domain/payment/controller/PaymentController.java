package com.business.i4_be.domain.payment.controller;

import com.business.i4_be.domain.payment.dto.request.PaymentReqDto;
import com.business.i4_be.domain.payment.dto.response.PaymentResDto;
import com.business.i4_be.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 전체 결제목록 조회
    @GetMapping("/api/v1/payments")
    public ResponseEntity<List<PaymentResDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // 유저의 결제 목록 조회 (여러건)
    @GetMapping("/api/v1/users/{userId}/payments")
    public ResponseEntity<List<PaymentResDto>> getUserPayments(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }

    // 유저의 특정 결제 기록 조회 (단건)
    @GetMapping("/api/v1/users/{userId}/payments/{paymentId}")
    public ResponseEntity<PaymentResDto> getUserPaymentDetail(
            @PathVariable Long userId,
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(paymentService.getUserPaymentDetail(userId, paymentId));
    }

    // 유저의 결제 기록 생성
    @PostMapping("/api/v1/users/{userId}/payments")
    public ResponseEntity<PaymentResDto> createPayment(
            @PathVariable Long userId,
            @RequestBody PaymentReqDto request
    ) {
        return ResponseEntity.ok(paymentService.createPayment(userId, request));
    }

    // 결제 취소
    @PatchMapping("/api/v1/users/{userId}/payments/{paymentId}")
    public ResponseEntity<PaymentResDto> cancelPayment(
            @PathVariable Long userId,
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(paymentService.cancelPayment(userId, paymentId));
    }
}
