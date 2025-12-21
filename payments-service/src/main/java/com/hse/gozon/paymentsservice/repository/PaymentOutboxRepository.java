package com.hse.gozon.paymentsservice.repository;

import com.hse.gozon.paymentsservice.model.PaymentInbox;
import com.hse.gozon.paymentsservice.model.PaymentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutbox, Long> {

    @Query("""
            SELECT po FROM PaymentOutbox
            WHERE po.processedAt IS NULL
            ORDER BY po.createdAt ASC
            """)
    List<PaymentOutbox> findUnprocessed();
}
