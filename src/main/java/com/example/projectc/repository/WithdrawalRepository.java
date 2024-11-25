package com.example.projectc.repository;

import com.example.projectc.entity.Withdrawal;
import com.example.projectc.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    List<Withdrawal> findByUserId(Long userId);
    
    List<Withdrawal> findByStatus(TransactionStatus status);
    
    List<Withdrawal> findByUserIdAndStatus(Long userId, TransactionStatus status);
    
    List<Withdrawal> findByRequestTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT w FROM Withdrawal w WHERE w.user.id = :userId AND w.requestTime BETWEEN :start AND :end")
    List<Withdrawal> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(w.amount) FROM Withdrawal w WHERE w.user.id = :userId AND w.status = :status")
    BigDecimal sumAmountByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") TransactionStatus status);
}
