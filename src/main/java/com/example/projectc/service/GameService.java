package com.example.projectc.service;

import com.example.projectc.entity.*;
import com.example.projectc.repository.GameRepository;
import com.example.projectc.repository.BettingRecordRepository;
import com.example.projectc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {
    private final GameRepository gameRepository;
    private final BettingRecordRepository bettingRecordRepository;
    private final TransactionService transactionService;
    private final UserService userService;

    @Transactional
    public Game createGame(String name, GameType type, BigDecimal minBet, BigDecimal maxBet) {
        Game game = new Game();
        game.setName(name);
        game.setType(type);
        game.setMinBet(minBet);
        game.setMaxBet(maxBet);
        game.setStatus(GameStatus.ACTIVE);
        game.setCreatedAt(LocalDateTime.now());

        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGameStatus(Long gameId, GameStatus status) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        
        game.setStatus(status);
        return gameRepository.save(game);
    }

    @Transactional
    public BettingRecord placeBet(Long userId, Long gameId, BigDecimal amount) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalArgumentException("Game is not active");
        }

        if (amount.compareTo(game.getMinBet()) < 0 || amount.compareTo(game.getMaxBet()) > 0) {
            throw new IllegalArgumentException("Bet amount is out of range");
        }

        // 칩 차감
        transactionService.createChipTransaction(
            userService.findUserById(userId),
            game,
            amount.negate(),
            ChipTransactionType.BET
        );

        BettingRecord betting = new BettingRecord();
        betting.setUser(userService.findUserById(userId));
        betting.setGame(game);
        betting.setAmount(amount);
        betting.setResult(BettingResult.PENDING);
        betting.setBettingTime(LocalDateTime.now());

        return bettingRecordRepository.save(betting);
    }

    @Transactional
    public BettingRecord settleBet(Long bettingId, BettingResult result, BigDecimal winAmount) {
        BettingRecord betting = bettingRecordRepository.findById(bettingId)
                .orElseThrow(() -> new IllegalArgumentException("Betting record not found"));

        if (betting.getResult() != BettingResult.PENDING) {
            throw new IllegalArgumentException("Betting is already settled");
        }

        betting.setResult(result);
        betting.setWinAmount(winAmount);
        betting.setSettlementTime(LocalDateTime.now());

        if (winAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 승리 금액 지급
            transactionService.createChipTransaction(
                betting.getUser(),
                betting.getGame(),
                winAmount,
                ChipTransactionType.WIN
            );
        }

        return bettingRecordRepository.save(betting);
    }

    public List<Game> findActiveGames() {
        return gameRepository.findByStatus(GameStatus.ACTIVE);
    }

    public List<BettingRecord> findUserBettingHistory(Long userId) {
        return bettingRecordRepository.findByUserId(userId);
    }

    public List<BettingRecord> findGameBettingHistory(Long gameId) {
        return bettingRecordRepository.findByGameId(gameId);
    }

    public BigDecimal calculateUserWinLoss(Long userId) {
        return bettingRecordRepository.calculateWinLossByUserId(userId);
    }

    public BigDecimal calculateGameRevenue(Long gameId) {
        return bettingRecordRepository.calculateRevenueByGameId(gameId);
    }
}
