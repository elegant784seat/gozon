package com.hse.gozon.paymentsservice.repository;

import com.hse.gozon.paymentsservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
