package com.example.projectc.repository;

import com.example.projectc.entity.Deposit;
import com.example.projectc.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByUserId(Long userId);
    
    List<Deposit> findByStatus(TransactionStatus status);
    
    List<Deposit> findByUserIdAndStatus(Long userId, TransactionStatus status);
    
    List<Deposit> findByRequestTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT d FROM Deposit d WHERE d.user.id = :userId AND d.requestTime BETWEEN :start AND :end")
    List<Deposit> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(d.amount) FROM Deposit d WHERE d.user.id = :userId AND d.status = :status")
    BigDecimal sumAmountByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") TransactionStatus status);
}
