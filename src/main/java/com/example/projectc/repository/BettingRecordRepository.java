package com.example.projectc.repository;

import com.example.projectc.entity.BettingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BettingRecordRepository extends JpaRepository<BettingRecord, Long> {
    List<BettingRecord> findByUserId(Long userId);
    
    List<BettingRecord> findByGameId(Long gameId);

    @Query("SELECT COALESCE(SUM(br.winAmount), 0) - COALESCE(SUM(br.amount), 0) " +
           "FROM BettingRecord br WHERE br.user.id = :userId")
    BigDecimal calculateWinLossByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(br.amount), 0) - COALESCE(SUM(br.winAmount), 0) " +
           "FROM BettingRecord br WHERE br.game.id = :gameId")
    BigDecimal calculateHouseEdgeByGameId(@Param("gameId") Long gameId);

    @Query("SELECT COALESCE(SUM(br.amount), 0) - COALESCE(SUM(br.winAmount), 0) " +
           "FROM BettingRecord br WHERE br.game.id = :gameId")
    BigDecimal calculateRevenueByGameId(@Param("gameId") Long gameId);

    @Query("SELECT COALESCE(SUM(br.amount), 0) FROM BettingRecord br " +
           "WHERE br.bettingTime BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalBettingAmountByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(br) FROM BettingRecord br " +
           "WHERE br.user.id = :userId AND br.bettingTime BETWEEN :startDate AND :endDate")
    long countBettingsByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT br.game.id, COUNT(br) as count, SUM(br.amount) as totalAmount " +
           "FROM BettingRecord br " +
           "GROUP BY br.game.id " +
           "ORDER BY totalAmount DESC")
    List<Object[]> findGameStatistics();
}
