package com.hse.gozon.paymentsservice.repository;

import com.hse.gozon.paymentsservice.model.PaymentInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentInboxRepository extends JpaRepository<PaymentInbox, Long> {
    @Query("""
            SELECT pi FROM PaymentInbox
            WHERE pi.processedAt IS NULL
            ORDER BY pi.createdAt ASC
            """)
    List<PaymentInbox> findUnprocessed();
}
