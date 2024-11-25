package com.example.projectc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "games",
    indexes = {
        @Index(name = "idx_game_type", columnList = "type"),
        @Index(name = "idx_game_status", columnList = "status"),
        @Index(name = "idx_game_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "min_bet", nullable = false)
    private BigDecimal minBet = BigDecimal.ZERO;

    @Column(name = "max_bet", nullable = false)
    private BigDecimal maxBet = BigDecimal.ZERO;

    @Column(name = "total_betting", nullable = false)
    private BigDecimal totalBetting = BigDecimal.ZERO;

    @Column(name = "total_payout", nullable = false)
    private BigDecimal totalPayout = BigDecimal.ZERO;

    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (minBet == null) minBet = BigDecimal.ZERO;
        if (maxBet == null) maxBet = BigDecimal.ZERO;
        if (totalBetting == null) totalBetting = BigDecimal.ZERO;
        if (totalPayout == null) totalPayout = BigDecimal.ZERO;
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
