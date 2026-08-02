package com.learnsphere.payment.controller;

import com.learnsphere.enrollment.entity.Enrollment;
import com.learnsphere.payment.dto.CreateOrderResponse;
import com.learnsphere.payment.dto.VerifyPaymentRequest;
import com.learnsphere.payment.service.PaymentService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    // =====================================================
    // ================= CONSTRUCTOR ========================
    // =====================================================

    public PaymentController(
            PaymentService paymentService
    ) {

        this.paymentService =
                paymentService;
    }


    // =====================================================
    // ================= CREATE ORDER =======================
    // =====================================================

    /*
     * Student clicks:
     *
     * Buy Course
     *
     * React calls:
     *
     * POST
     * /api/payments/courses/{courseId}/order
     *
     * Spring Boot creates Razorpay Order
     * and returns order information to React.
     */

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/courses/{courseId}/order")
    public CreateOrderResponse createOrder(
            @PathVariable Long courseId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return paymentService
                .createOrder(
                        email,
                        courseId
                );
    }


    // =====================================================
    // ================= VERIFY PAYMENT =====================
    // =====================================================

    /*
     * After Razorpay Checkout succeeds,
     * React sends:
     *
     * razorpayOrderId
     * razorpayPaymentId
     * razorpaySignature
     *
     * Spring Boot verifies the signature.
     *
     * Only after successful verification
     * will the student be enrolled.
     */

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/verify")
    public Enrollment verifyPayment(
            @RequestBody VerifyPaymentRequest request,
            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return paymentService
                .verifyPayment(
                        email,
                        request
                );
    }
}