package com.example.projectc.controller;

import com.example.projectc.entity.*;
import com.example.projectc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposits")
    public ResponseEntity<Deposit> createDeposit(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String bankName = (String) request.get("bankName");
        String accountNumber = (String) request.get("accountNumber");
        String accountHolder = (String) request.get("accountHolder");
        
        Deposit deposit = transactionService.createDeposit(userId, amount, bankName, accountNumber, accountHolder);
        return ResponseEntity.ok(deposit);
    }

    @PostMapping("/deposits/{depositId}/approve")
    public ResponseEntity<Deposit> approveDeposit(
            @PathVariable Long depositId,
            @RequestParam Long adminId) {
        Deposit deposit = transactionService.approveDeposit(depositId, adminId);
        return ResponseEntity.ok(deposit);
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<Withdrawal> createWithdrawal(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String bankName = (String) request.get("bankName");
        String accountNumber = (String) request.get("accountNumber");
        String accountHolder = (String) request.get("accountHolder");
        
        Withdrawal withdrawal = transactionService.createWithdrawal(userId, amount, bankName, accountNumber, accountHolder);
        return ResponseEntity.ok(withdrawal);
    }

    @PostMapping("/withdrawals/{withdrawalId}/approve")
    public ResponseEntity<Withdrawal> approveWithdrawal(
            @PathVariable Long withdrawalId,
            @RequestParam Long adminId) {
        Withdrawal withdrawal = transactionService.approveWithdrawal(withdrawalId, adminId);
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("/deposits/pending")
    public ResponseEntity<List<Deposit>> getPendingDeposits() {
        List<Deposit> deposits = transactionService.findPendingDeposits();
        return ResponseEntity.ok(deposits);
    }

    @GetMapping("/withdrawals/pending")
    public ResponseEntity<List<Withdrawal>> getPendingWithdrawals() {
        List<Withdrawal> withdrawals = transactionService.findPendingWithdrawals();
        return ResponseEntity.ok(withdrawals);
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<ChipTransaction>> getUserTransactions(@PathVariable Long userId) {
        List<ChipTransaction> transactions = transactionService.findUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/users/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        BigDecimal balance = transactionService.getCurrentBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/users/{userId}/deposit-sum")
    public ResponseEntity<BigDecimal> getUserDepositSum(@PathVariable Long userId) {
        BigDecimal sum = transactionService.getDepositSum(userId);
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/users/{userId}/withdrawal-sum")
    public ResponseEntity<BigDecimal> getUserWithdrawalSum(@PathVariable Long userId) {
        BigDecimal sum = transactionService.getWithdrawalSum(userId);
        return ResponseEntity.ok(sum);
    }
}
