package com.example.projectc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "betting_records",
    indexes = {
        @Index(name = "idx_betting_user", columnList = "user_id"),
        @Index(name = "idx_betting_game", columnList = "game_id"),
        @Index(name = "idx_betting_result", columnList = "result"),
        @Index(name = "idx_betting_time", columnList = "bet_time")
    }
)
@Getter
@Setter
public class BettingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "bet_amount", nullable = false)
    private BigDecimal betAmount;

    @Column(name = "win_amount")
    private BigDecimal winAmount = BigDecimal.ZERO;

    @Column(name = "profit_amount")
    private BigDecimal profitAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BettingResult result;

    @Column(name = "bet_time", nullable = false, updatable = false)
    private LocalDateTime betTime;

    @Column(name = "result_time")
    private LocalDateTime resultTime;

    @Column(name = "bet_ip", length = 45)
    private String betIp;

    @Column(name = "details", length = 1000)
    private String details;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        betTime = LocalDateTime.now();
        if (winAmount == null) winAmount = BigDecimal.ZERO;
        if (profitAmount == null) profitAmount = BigDecimal.ZERO;
        if (result == null) result = BettingResult.PENDING;
    }

    @PostPersist
    protected void afterCreate() {
        if (user != null) {
            user.setTotalBetting(user.getTotalBetting().add(betAmount));
        }
        if (game != null) {
            game.setTotalBetting(game.getTotalBetting().add(betAmount));
        }
    }

    @PostUpdate
    protected void afterUpdate() {
        if (result == BettingResult.WIN && user != null) {
            user.setTotalWinning(user.getTotalWinning().add(winAmount));
            game.setTotalPayout(game.getTotalPayout().add(winAmount));
            game.setTotalRevenue(game.getTotalBetting().subtract(game.getTotalPayout()));
        }
    }
}
