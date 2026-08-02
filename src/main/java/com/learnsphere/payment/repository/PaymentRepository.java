package com.learnsphere.payment.repository;

import com.learnsphere.payment.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PaymentRepository
        extends JpaRepository<Payment, Long> {


    // =====================================================
    // ================= FIND BY ORDER ID ===================
    // =====================================================

    Optional<Payment> findByRazorpayOrderId(
            String razorpayOrderId
    );


    // =====================================================
    // ================= FIND BY PAYMENT ID =================
    // =====================================================

    Optional<Payment> findByRazorpayPaymentId(
            String razorpayPaymentId
    );


    // =====================================================
    // ================ USER PAYMENT HISTORY ===============
    // =====================================================

    List<Payment> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );


    // =====================================================
    // ============ SUCCESSFUL COURSE PAYMENT ==============
    // =====================================================

    boolean existsByUserIdAndCourseIdAndStatus(
            Long userId,
            Long courseId,
            String status
    );
}
