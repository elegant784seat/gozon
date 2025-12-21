package com.hse.gozon.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_outbox")
public class OrderOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "event_id", unique = true, nullable = false)
    private UUID eventId;
    @Column(nullable = false)
    private String payload;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
