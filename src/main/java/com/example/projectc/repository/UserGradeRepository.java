package com.example.projectc.repository;

import com.example.projectc.entity.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGradeRepository extends JpaRepository<UserGrade, Long> {
    Optional<UserGrade> findByName(String name);
    
    List<UserGrade> findByLevelGreaterThanEqual(Integer level);
    
    List<UserGrade> findByLevelLessThanEqual(Integer level);
    
    Optional<UserGrade> findFirstByOrderByLevelDesc();
    
    Optional<UserGrade> findFirstByOrderByLevelAsc();
}
