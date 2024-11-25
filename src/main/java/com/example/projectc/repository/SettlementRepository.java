package com.example.projectc.repository;

import com.example.projectc.entity.Settlement;
import com.example.projectc.entity.SettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByUserId(Long userId);
    
    List<Settlement> findByStatus(SettlementStatus status);
    
    List<Settlement> findByUserIdAndStatus(Long userId, SettlementStatus status);
    
    List<Settlement> findByProcessedById(Long processedById);
    
    List<Settlement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT s FROM Settlement s WHERE s.user.id = :userId AND s.createdAt BETWEEN :start AND :end")
    List<Settlement> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
            
    @Query("SELECT SUM(s.commissionAmount) FROM Settlement s WHERE s.user.id = :userId AND s.status = :status")
    BigDecimal sumCommissionByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") SettlementStatus status);
            
    @Query("SELECT s FROM Settlement s WHERE " +
           "s.settlementPeriodStart <= :date AND " +
           "s.settlementPeriodEnd >= :date AND " +
           "s.user.id = :userId")
    List<Settlement> findByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(s) FROM Settlement s WHERE s.status = :status")
    long countByStatus(@Param("status") SettlementStatus status);

    @Query("SELECT SUM(s.amount) FROM Settlement s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(CASE WHEN s.processedAt IS NOT NULL THEN TIMESTAMPDIFF(SECOND, s.createdAt, s.processedAt) ELSE NULL END) " +
           "FROM Settlement s WHERE s.status IN ('APPROVED', 'REJECTED')")
    Double calculateAverageProcessingTime();
}
