package com.business.i4_be.domain.payment.repository;

import com.business.i4_be.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
