package com.example.projectc.service;

import com.example.projectc.entity.*;
import com.example.projectc.repository.UserRepository;
import com.example.projectc.repository.UserGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserGradeRepository userGradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String username, String password, String phone, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(UserRole.valueOf(role));
        user.setStatus(UserStatus.ACTIVE);
        user.setRegistrationStatus(RegistrationStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(User user, Long parentId) {
        // 사용자 이름 중복 체크
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (parentId != null) {
            User parent = findUserById(parentId);
            user.setParent(parent);
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.PENDING);
        user.setRegistrationStatus(RegistrationStatus.REQUESTED);
        
        // 기본 등급 설정
        userGradeRepository.findFirstByOrderByLevelAsc()
                .ifPresent(user::setGrade);

        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<User> findUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> findUsersByGrade(Long gradeId) {
        UserGrade grade = userGradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));
        return userRepository.findByGrade(grade);
    }

    public List<UserGrade> findAllGrades() {
        return userGradeRepository.findAll();
    }

    @Transactional
    public User updateUserStatus(Long userId, UserStatus status) {
        User user = findUserById(userId);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserGrade(Long userId, Long gradeId) {
        User user = findUserById(userId);
        UserGrade grade = userGradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));
        user.setGrade(grade);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserRegistrationStatus(Long userId, RegistrationStatus status, Long approvedById) {
        User user = findUserById(userId);
        user.setRegistrationStatus(status);
        if (approvedById != null) {
            User approvedBy = findUserById(approvedById);
            user.setApprovedBy(approvedBy);
            user.setApprovedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void updateLastLogin(Long userId, String ipAddress) {
        User user = findUserById(userId);
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public User approveUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can approve users");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setRegistrationStatus(RegistrationStatus.APPROVED);
        user.setApprovedBy(admin);
        user.setApprovedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> findByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> findByRegistrationStatus(RegistrationStatus status) {
        return userRepository.findByRegistrationStatus(status);
    }

    public List<User> findReferrals(Long userId) {
        return userRepository.findByParentId(userId);
    }

    public long countReferrals(Long userId) {
        return userRepository.countReferrals(userId);
    }
}
