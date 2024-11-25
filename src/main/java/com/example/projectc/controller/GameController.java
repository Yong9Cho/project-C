package com.example.projectc.controller;

import com.example.projectc.entity.*;
import com.example.projectc.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Map<String, Object> request) {
        String name = request.get("name").toString();
        GameType type = GameType.valueOf(request.get("type").toString());
        BigDecimal minBet = new BigDecimal(request.get("minBet").toString());
        BigDecimal maxBet = new BigDecimal(request.get("maxBet").toString());
        
        Game game = gameService.createGame(name, type, minBet, maxBet);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{gameId}/status")
    public ResponseEntity<Game> updateGameStatus(
            @PathVariable Long gameId,
            @RequestParam GameStatus status) {
        Game game = gameService.updateGameStatus(gameId, status);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/bet")
    public ResponseEntity<BettingRecord> placeBet(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long gameId = Long.valueOf(request.get("gameId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        
        BettingRecord betting = gameService.placeBet(userId, gameId, amount);
        return ResponseEntity.ok(betting);
    }

    @PostMapping("/bet/{bettingId}/settle")
    public ResponseEntity<BettingRecord> settleBet(
            @PathVariable Long bettingId,
            @RequestBody Map<String, Object> request) {
        BettingResult result = BettingResult.valueOf(request.get("result").toString());
        BigDecimal winAmount = new BigDecimal(request.get("winAmount").toString());
        
        BettingRecord betting = gameService.settleBet(bettingId, result, winAmount);
        return ResponseEntity.ok(betting);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Game>> getActiveGames() {
        List<Game> games = gameService.findActiveGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/users/{userId}/betting-history")
    public ResponseEntity<List<BettingRecord>> getUserBettingHistory(@PathVariable Long userId) {
        List<BettingRecord> history = gameService.findUserBettingHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{gameId}/betting-history")
    public ResponseEntity<List<BettingRecord>> getGameBettingHistory(@PathVariable Long gameId) {
        List<BettingRecord> history = gameService.findGameBettingHistory(gameId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/users/{userId}/win-loss")
    public ResponseEntity<BigDecimal> getUserWinLoss(@PathVariable Long userId) {
        BigDecimal winLoss = gameService.calculateUserWinLoss(userId);
        return ResponseEntity.ok(winLoss);
    }

    @GetMapping("/{gameId}/revenue")
    public ResponseEntity<BigDecimal> getGameRevenue(@PathVariable Long gameId) {
        BigDecimal revenue = gameService.calculateGameRevenue(gameId);
        return ResponseEntity.ok(revenue);
    }
}
