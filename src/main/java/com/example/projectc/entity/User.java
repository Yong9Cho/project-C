package com.example.projectc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "users",
    indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_registration_status", columnList = "registration_status"),
        @Index(name = "idx_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "registration_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private UserGrade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private User parent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "total_deposit", nullable = false)
    private BigDecimal totalDeposit = BigDecimal.ZERO;

    @Column(name = "total_withdrawal", nullable = false)
    private BigDecimal totalWithdrawal = BigDecimal.ZERO;

    @Column(name = "total_betting", nullable = false)
    private BigDecimal totalBetting = BigDecimal.ZERO;

    @Column(name = "total_winning", nullable = false)
    private BigDecimal totalWinning = BigDecimal.ZERO;

    @Column(length = 500)
    private String memo;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentBalance == null) currentBalance = BigDecimal.ZERO;
        if (totalDeposit == null) totalDeposit = BigDecimal.ZERO;
        if (totalWithdrawal == null) totalWithdrawal = BigDecimal.ZERO;
        if (totalBetting == null) totalBetting = BigDecimal.ZERO;
        if (totalWinning == null) totalWinning = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addBalance(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.currentBalance = this.currentBalance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void subtractBalance(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (this.currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.currentBalance = this.currentBalance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void addTotalDeposit(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.totalDeposit = this.totalDeposit.add(amount);
    }

    public void addTotalWithdrawal(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.totalWithdrawal = this.totalWithdrawal.add(amount);
    }

    public void addTotalBetting(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.totalBetting = this.totalBetting.add(amount);
    }

    public void addTotalWinning(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.totalWinning = this.totalWinning.add(amount);
    }
}
