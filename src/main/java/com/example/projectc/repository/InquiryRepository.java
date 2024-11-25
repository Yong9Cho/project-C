package com.example.projectc.repository;

import com.example.projectc.entity.Inquiry;
import com.example.projectc.entity.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long userId);
    
    List<Inquiry> findByStatus(InquiryStatus status);
    
    List<Inquiry> findByUserIdAndStatus(Long userId, InquiryStatus status);
    
    List<Inquiry> findByAnsweredById(Long answeredById);
    
    List<Inquiry> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT i FROM Inquiry i WHERE i.user.id = :userId AND i.createdAt BETWEEN :start AND :end")
    List<Inquiry> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
            
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.user.id = :userId AND i.status = :status")
    long countByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") InquiryStatus status);

    Long countByStatus(InquiryStatus status);

    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, i.createdAt, i.answeredAt)) " +
           "FROM Inquiry i WHERE i.status = 'ANSWERED'")
    Double calculateAverageResponseTime();
}
