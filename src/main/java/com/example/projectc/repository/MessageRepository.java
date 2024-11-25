package com.example.projectc.repository;

import com.example.projectc.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);
    
    List<Message> findByReceiverId(Long receiverId);
    
    List<Message> findByReceiverIdAndRead(Long receiverId, boolean read);
    
    List<Message> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId OR m.receiver.id = :userId) AND m.createdAt BETWEEN :start AND :end")
    List<Message> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
            
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.read = false")
    long countUnreadMessages(@Param("userId") Long userId);
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
           "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findConversation(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id);
}
