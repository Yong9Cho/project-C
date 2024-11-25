package com.example.projectc.repository;

import com.example.projectc.entity.User;
import com.example.projectc.entity.UserRole;
import com.example.projectc.entity.UserStatus;
import com.example.projectc.entity.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByStatus(UserStatus status);
    
    List<User> findByRegistrationStatus(RegistrationStatus registrationStatus);
    
    List<User> findByParentId(Long parentId);
    
    boolean existsByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.parent.id = :parentId AND u.role = :role")
    List<User> findByParentIdAndRole(@Param("parentId") Long parentId, @Param("role") UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.parent.id = :userId")
    long countReferrals(@Param("userId") Long userId);
}
