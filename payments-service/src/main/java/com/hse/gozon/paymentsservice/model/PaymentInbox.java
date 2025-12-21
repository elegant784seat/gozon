package com.hse.gozon.paymentsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_inbox")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;
    private String payload;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
