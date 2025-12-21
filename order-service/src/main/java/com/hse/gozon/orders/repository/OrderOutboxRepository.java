package com.hse.gozon.orders.repository;

import com.hse.gozon.orders.model.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {

    @Query("""
            SELECT o FROM OrderOutbox o
            WHERE o.processedAt IS NULL
            ORDER BY o.createdAt ASC
            """)
    List<OrderOutbox> findUnprocessed();
}
