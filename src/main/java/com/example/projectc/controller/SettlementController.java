package com.example.projectc.controller;

import com.example.projectc.entity.*;
import com.example.projectc.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<Settlement> createSettlement(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        LocalDateTime startDate = LocalDateTime.parse(request.get("startDate").toString());
        LocalDateTime endDate = LocalDateTime.parse(request.get("endDate").toString());
        
        Settlement settlement = settlementService.createSettlement(userId, startDate, endDate);
        return ResponseEntity.ok(settlement);
    }

    @PostMapping("/{settlementId}/approve")
    public ResponseEntity<Settlement> approveSettlement(
            @PathVariable Long settlementId,
            @RequestParam Long adminId) {
        Settlement settlement = settlementService.approveSettlement(settlementId, adminId);
        return ResponseEntity.ok(settlement);
    }

    @PostMapping("/{settlementId}/reject")
    public ResponseEntity<Settlement> rejectSettlement(
            @PathVariable Long settlementId,
            @RequestBody Map<String, Object> request) {
        Long adminId = Long.valueOf(request.get("adminId").toString());
        String reason = request.get("reason").toString();
        
        Settlement settlement = settlementService.rejectSettlement(settlementId, adminId, reason);
        return ResponseEntity.ok(settlement);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Settlement>> getUserSettlements(@PathVariable Long userId) {
        List<Settlement> settlements = settlementService.findUserSettlements(userId);
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Settlement>> getPendingSettlements() {
        List<Settlement> settlements = settlementService.findPendingSettlements();
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Settlement>> getSettlementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Settlement> settlements = settlementService.findSettlementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalSettlementAmount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        BigDecimal totalAmount = settlementService.calculateTotalSettlementAmount(startDate, endDate);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/count/pending")
    public ResponseEntity<Long> getPendingSettlementsCount() {
        long count = settlementService.countPendingSettlements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/processing-time")
    public ResponseEntity<Double> getAverageProcessingTime() {
        double avgTime = settlementService.calculateAverageProcessingTime();
        return ResponseEntity.ok(avgTime);
    }
}
