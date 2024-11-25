package com.example.projectc.service;

import com.example.projectc.entity.*;
import com.example.projectc.repository.SettlementRepository;
import com.example.projectc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;
    private final GameService gameService;

    @Transactional
    public Settlement createSettlement(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 해당 기간의 승/패 금액 계산
        BigDecimal winLossAmount = gameService.calculateUserWinLoss(userId);

        Settlement settlement = new Settlement();
        settlement.setUser(user);
        settlement.setStartDate(startDate);
        settlement.setEndDate(endDate);
        settlement.setAmount(winLossAmount);
        settlement.setStatus(SettlementStatus.PENDING);
        settlement.setCreatedAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Transactional
    public Settlement approveSettlement(Long settlementId, Long adminId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new IllegalArgumentException("Settlement not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can approve settlements");
        }

        settlement.setStatus(SettlementStatus.APPROVED);
        settlement.setApprovedBy(admin);
        settlement.setApprovedAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Transactional
    public Settlement rejectSettlement(Long settlementId, Long adminId, String reason) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new IllegalArgumentException("Settlement not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can reject settlements");
        }

        settlement.setStatus(SettlementStatus.REJECTED);
        settlement.setRejectedBy(admin);
        settlement.setRejectedAt(LocalDateTime.now());
        settlement.setRejectionReason(reason);

        return settlementRepository.save(settlement);
    }

    public List<Settlement> findUserSettlements(Long userId) {
        return settlementRepository.findByUserId(userId);
    }

    public List<Settlement> findPendingSettlements() {
        return settlementRepository.findByStatus(SettlementStatus.PENDING);
    }

    public List<Settlement> findSettlementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return settlementRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public BigDecimal calculateTotalSettlementAmount(LocalDateTime startDate, LocalDateTime endDate) {
        return settlementRepository.calculateTotalAmountByDateRange(startDate, endDate);
    }

    public long countPendingSettlements() {
        return settlementRepository.countByStatus(SettlementStatus.PENDING);
    }

    public double calculateAverageProcessingTime() {
        return settlementRepository.calculateAverageProcessingTime();
    }
}
