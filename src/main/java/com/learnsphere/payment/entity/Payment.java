package com.learnsphere.payment.entity;

import com.learnsphere.course.entity.Course;
import com.learnsphere.entity.User;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_razorpay_order",
                        columnList = "razorpay_order_id"
                ),
                @Index(
                        name = "idx_payment_razorpay_payment",
                        columnList = "razorpay_payment_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {


    // =====================================================
    // ================= PRIMARY KEY ========================
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // ================= STUDENT ============================
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    // =====================================================
    // ================= COURSE =============================
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;


    // =====================================================
    // ================= PAYMENT AMOUNT =====================
    // =====================================================

    // Course price in rupees.
    //
    // Example:
    // ₹499.00
    //
    // Razorpay order amount will later be created
    // in paise:
    //
    // 499.00 rupees -> 49900 paise

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;


    // =====================================================
    // ================= CURRENCY ===========================
    // =====================================================

    @Column(
            nullable = false,
            length = 10
    )
    private String currency;


    // =====================================================
    // ================= RAZORPAY ORDER ID ==================
    // =====================================================

    @Column(
            name = "razorpay_order_id",
            unique = true,
            length = 100
    )
    private String razorpayOrderId;


    // =====================================================
    // ================= RAZORPAY PAYMENT ID ================
    // =====================================================

    @Column(
            name = "razorpay_payment_id",
            unique = true,
            length = 100
    )
    private String razorpayPaymentId;


    // =====================================================
    // ================= PAYMENT STATUS =====================
    // =====================================================

    /*
     * Possible LearnSphere statuses:
     *
     * CREATED
     * SUCCESS
     * FAILED
     */

    @Column(
            nullable = false,
            length = 30
    )
    private String status;


    // =====================================================
    // ================= CREATED TIME =======================
    // =====================================================

    @Column(nullable = false)
    private LocalDateTime createdAt;


    // =====================================================
    // ================= PAID TIME ==========================
    // =====================================================

    private LocalDateTime paidAt;
}