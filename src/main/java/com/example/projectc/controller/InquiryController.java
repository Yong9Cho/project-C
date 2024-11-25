package com.example.projectc.controller;

import com.example.projectc.entity.*;
import com.example.projectc.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<Inquiry> createInquiry(@RequestBody Map<String, String> request) {
        Long userId = Long.valueOf(request.get("userId"));
        String title = request.get("title");
        String content = request.get("content");
        
        Inquiry inquiry = inquiryService.createInquiry(userId, title, content);
        return ResponseEntity.ok(inquiry);
    }

    @PostMapping("/{inquiryId}/messages")
    public ResponseEntity<Message> addMessage(
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> request) {
        Long userId = Long.valueOf(request.get("userId"));
        String content = request.get("content");
        
        Message message = inquiryService.addMessage(inquiryId, userId, content);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/{inquiryId}/close")
    public ResponseEntity<Inquiry> closeInquiry(
            @PathVariable Long inquiryId,
            @RequestParam Long adminId) {
        Inquiry inquiry = inquiryService.closeInquiry(inquiryId, adminId);
        return ResponseEntity.ok(inquiry);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Inquiry>> getUserInquiries(@PathVariable Long userId) {
        List<Inquiry> inquiries = inquiryService.findUserInquiries(userId);
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/{inquiryId}/messages")
    public ResponseEntity<List<Message>> getInquiryMessages(@PathVariable Long inquiryId) {
        List<Message> messages = inquiryService.findInquiryMessages(inquiryId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/status/open")
    public ResponseEntity<List<Inquiry>> getOpenInquiries() {
        List<Inquiry> inquiries = inquiryService.findOpenInquiries();
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/status/answered")
    public ResponseEntity<List<Inquiry>> getAnsweredInquiries() {
        List<Inquiry> inquiries = inquiryService.findAnsweredInquiries();
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/status/closed")
    public ResponseEntity<List<Inquiry>> getClosedInquiries() {
        List<Inquiry> inquiries = inquiryService.findClosedInquiries();
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/count/open")
    public ResponseEntity<Long> getOpenInquiriesCount() {
        long count = inquiryService.countOpenInquiries();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/response-time")
    public ResponseEntity<Double> getAverageResponseTime() {
        double avgTime = inquiryService.calculateAverageResponseTime();
        return ResponseEntity.ok(avgTime);
    }
}
