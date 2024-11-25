package com.example.projectc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
@Getter
@Setter
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "settlement_period_start", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "settlement_period_end", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "total_bet_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "total_win_amount", nullable = false)
    private BigDecimal totalWinAmount;

    @Column(name = "commission_rate", nullable = false)
    private BigDecimal commissionRate;

    @Column(name = "commission_amount", nullable = false)
    private BigDecimal commissionAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SettlementStatus status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = SettlementStatus.PENDING;
    }
}
