package com.example.projectc.service;

import com.example.projectc.entity.*;
import com.example.projectc.repository.InquiryRepository;
import com.example.projectc.repository.MessageRepository;
import com.example.projectc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Inquiry createInquiry(Long userId, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setUser(user);
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setStatus(InquiryStatus.OPEN);
        inquiry.setCreatedAt(LocalDateTime.now());

        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Message addMessage(Long inquiryId, Long userId, String content) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message();
        message.setInquiry(inquiry);
        message.setUser(user);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        // 관리자가 답변을 작성한 경우, 문의 상태를 답변완료로 변경
        if (user.getRole() == UserRole.ADMIN) {
            inquiry.setStatus(InquiryStatus.ANSWERED);
            inquiry.setAnsweredAt(LocalDateTime.now());
            inquiry.setAnsweredBy(user);
            inquiryRepository.save(inquiry);
        }

        return messageRepository.save(message);
    }

    @Transactional
    public Inquiry closeInquiry(Long inquiryId, Long adminId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can close inquiries");
        }

        inquiry.setStatus(InquiryStatus.CLOSED);
        inquiry.setClosedAt(LocalDateTime.now());
        inquiry.setClosedBy(admin);

        return inquiryRepository.save(inquiry);
    }

    public List<Inquiry> findUserInquiries(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    public List<Message> findInquiryMessages(Long inquiryId) {
        return messageRepository.findByInquiryId(inquiryId);
    }

    public List<Inquiry> findOpenInquiries() {
        return inquiryRepository.findByStatus(InquiryStatus.OPEN);
    }

    public List<Inquiry> findAnsweredInquiries() {
        return inquiryRepository.findByStatus(InquiryStatus.ANSWERED);
    }

    public List<Inquiry> findClosedInquiries() {
        return inquiryRepository.findByStatus(InquiryStatus.CLOSED);
    }

    public long countOpenInquiries() {
        return inquiryRepository.countByStatus(InquiryStatus.OPEN);
    }

    public double calculateAverageResponseTime() {
        return inquiryRepository.calculateAverageResponseTime();
    }
}
