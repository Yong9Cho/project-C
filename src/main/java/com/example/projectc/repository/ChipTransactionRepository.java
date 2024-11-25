package com.example.projectc.repository;

import com.example.projectc.entity.ChipTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ChipTransactionRepository extends JpaRepository<ChipTransaction, Long> {
    List<ChipTransaction> findByUserId(Long userId);

    @Query("SELECT COALESCE(MAX(ct.balanceAfter), 0) FROM ChipTransaction ct WHERE ct.user.id = :userId")
    BigDecimal findLatestBalanceByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(CASE WHEN ct.transactionType = 'WIN' THEN ct.amount ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN ct.transactionType = 'BET' THEN ct.amount ELSE 0 END), 0) " +
           "FROM ChipTransaction ct WHERE ct.user.id = :userId")
    BigDecimal calculateWinLossByUserId(@Param("userId") Long userId);
}
