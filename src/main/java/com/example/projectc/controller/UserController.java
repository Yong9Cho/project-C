package com.example.projectc.controller;

import com.example.projectc.entity.User;
import com.example.projectc.entity.UserGrade;
import com.example.projectc.entity.UserRole;
import com.example.projectc.entity.UserStatus;
import com.example.projectc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> request) {
        User user = userService.createUser(
            request.get("username"),
            request.get("password"),
            request.get("nickname"),
            request.get("phoneNumber")
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/approve")
    public ResponseEntity<User> approveUser(
            @PathVariable Long userId,
            @RequestParam Long adminId) {
        User user = userService.approveUser(userId, adminId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam UserStatus status) {
        User user = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/grade")
    public ResponseEntity<User> updateUserGrade(
            @PathVariable Long userId,
            @RequestParam Long gradeId) {
        User user = userService.updateUserGrade(userId, gradeId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable UserStatus status) {
        List<User> users = userService.findUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        List<User> users = userService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/grade/{gradeId}")
    public ResponseEntity<List<User>> getUsersByGrade(@PathVariable Long gradeId) {
        List<User> users = userService.findUsersByGrade(gradeId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/grades")
    public ResponseEntity<List<UserGrade>> getAllGrades() {
        List<UserGrade> grades = userService.findAllGrades();
        return ResponseEntity.ok(grades);
    }
}
