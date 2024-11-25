package com.example.projectc.repository;

import com.example.projectc.entity.Game;
import com.example.projectc.entity.GameStatus;
import com.example.projectc.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);
    
    List<Game> findByType(GameType type);
    
    List<Game> findByStatus(GameStatus status);
    
    List<Game> findByTypeAndStatus(GameType type, GameStatus status);
    
    boolean existsByName(String name);
}
