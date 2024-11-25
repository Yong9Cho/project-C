package com.example.projectc.service;

import com.example.projectc.entity.*;
import com.example.projectc.repository.DepositRepository;
import com.example.projectc.repository.WithdrawalRepository;
import com.example.projectc.repository.ChipTransactionRepository;
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
public class TransactionService {
    private final DepositRepository depositRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final ChipTransactionRepository chipTransactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Deposit createDeposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deposit deposit = new Deposit();
        deposit.setUser(user);
        deposit.setAmount(amount);
        deposit.setStatus(TransactionStatus.PENDING);

        return depositRepository.save(deposit);
    }

    @Transactional
    public Deposit approveDeposit(Long depositId, Long adminId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can approve deposits");
        }

        deposit.setStatus(TransactionStatus.APPROVED);
        deposit.setApprovedBy(admin);
        deposit.setProcessedTime(LocalDateTime.now());

        // 칩 거래 내역 생성
        createChipTransaction(
            deposit.getUser(),
            null,
            deposit.getAmount(),
            ChipTransactionType.DEPOSIT
        );

        return depositRepository.save(deposit);
    }

    @Transactional
    public Withdrawal createWithdrawal(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 현재 잔액 확인
        BigDecimal balance = getCurrentBalance(userId);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUser(user);
        withdrawal.setAmount(amount);
        withdrawal.setStatus(TransactionStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Transactional
    public Withdrawal approveWithdrawal(Long withdrawalId, Long adminId) {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new IllegalArgumentException("Withdrawal not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can approve withdrawals");
        }

        withdrawal.setStatus(TransactionStatus.APPROVED);
        withdrawal.setApprovedBy(admin);
        withdrawal.setProcessedTime(LocalDateTime.now());

        // 칩 거래 내역 생성
        createChipTransaction(
            withdrawal.getUser(),
            null,
            withdrawal.getAmount().negate(),
            ChipTransactionType.WITHDRAWAL
        );

        return withdrawalRepository.save(withdrawal);
    }

    @Transactional
    public ChipTransaction createChipTransaction(User user, Game game, BigDecimal amount, ChipTransactionType type) {
        BigDecimal currentBalance = getCurrentBalance(user.getId());
        BigDecimal newBalance = currentBalance.add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        ChipTransaction transaction = new ChipTransaction();
        transaction.setUser(user);
        transaction.setGame(game);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setBalanceAfter(newBalance);

        return chipTransactionRepository.save(transaction);
    }

    public BigDecimal getCurrentBalance(Long userId) {
        return chipTransactionRepository.findLatestBalanceByUserId(userId);
    }

    public List<Deposit> findPendingDeposits() {
        return depositRepository.findByStatus(TransactionStatus.PENDING);
    }

    public List<Withdrawal> findPendingWithdrawals() {
        return withdrawalRepository.findByStatus(TransactionStatus.PENDING);
    }

    public List<ChipTransaction> findUserTransactions(Long userId) {
        return chipTransactionRepository.findByUserId(userId);
    }

    public BigDecimal getDepositSum(Long userId) {
        return depositRepository.sumAmountByUserIdAndStatus(userId, TransactionStatus.APPROVED);
    }

    public BigDecimal getWithdrawalSum(Long userId) {
        return withdrawalRepository.sumAmountByUserIdAndStatus(userId, TransactionStatus.APPROVED);
    }
}
