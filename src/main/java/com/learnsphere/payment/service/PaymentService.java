package com.learnsphere.payment.service;

import com.learnsphere.course.entity.Course;
import com.learnsphere.course.service.CourseService;
import com.learnsphere.enrollment.entity.Enrollment;
import com.learnsphere.enrollment.repository.EnrollmentRepository;
import com.learnsphere.enrollment.service.EnrollmentService;
import com.learnsphere.entity.User;
import com.learnsphere.payment.dto.CreateOrderResponse;
import com.learnsphere.payment.dto.VerifyPaymentRequest;
import com.learnsphere.payment.entity.Payment;
import com.learnsphere.payment.repository.PaymentRepository;
import com.learnsphere.repository.UserRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;


    // =====================================================
    // ================= PAYMENT MODE =======================
    // =====================================================

    /*
     * mock      -> Development/demo payment
     * razorpay  -> Real Razorpay integration
     */

    @Value("${payment.mode:mock}")
    private String paymentMode;


    // =====================================================
    // ================= RAZORPAY CONFIG ====================
    // =====================================================

    /*
     * Empty defaults allow the application to start
     * while we are using MOCK mode.
     */

    @Value("${razorpay.key.id:}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:}")
    private String razorpayKeySecret;


    // =====================================================
    // ================= CONSTRUCTOR ========================
    // =====================================================

    public PaymentService(
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            CourseService courseService,
            EnrollmentRepository enrollmentRepository,
            EnrollmentService enrollmentService
    ) {

        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentService = enrollmentService;
    }


    // =====================================================
    // ================= CREATE ORDER =======================
    // =====================================================

    @Transactional
    public CreateOrderResponse createOrder(
            String email,
            Long courseId
    ) {

        try {

            // ================= FIND USER =================

            User user = userRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );


            // ================= FIND COURSE =================

            Course course =
                    courseService.getCourseById(courseId);


            if (course == null) {

                throw new RuntimeException(
                        "Course not found"
                );
            }


            // ================= CHECK ENROLLMENT =================

            boolean alreadyEnrolled =
                    enrollmentRepository
                            .existsByUserIdAndCourseId(
                                    user.getId(),
                                    courseId
                            );


            if (alreadyEnrolled) {

                throw new RuntimeException(
                        "You are already enrolled in this course"
                );
            }


            // ================= CHECK PAYMENT =================

            boolean alreadyPaid =
                    paymentRepository
                            .existsByUserIdAndCourseIdAndStatus(
                                    user.getId(),
                                    courseId,
                                    "SUCCESS"
                            );


            if (alreadyPaid) {

                throw new RuntimeException(
                        "Payment has already been completed for this course"
                );
            }


            // ================= VALIDATE PRICE =================

            BigDecimal amount =
                    course.getPrice();


            if (amount == null) {

                throw new RuntimeException(
                        "Course price is not available"
                );
            }


            if (amount.compareTo(BigDecimal.ZERO) <= 0) {

                throw new RuntimeException(
                        "Course price must be greater than zero"
                );
            }


            // ================= CONVERT TO PAISE =================

            long amountInPaise =
                    amount
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(
                                    0,
                                    RoundingMode.HALF_UP
                            )
                            .longValueExact();


            // =================================================
            // ================= MOCK MODE ======================
            // =================================================

            if (isMockMode()) {

                return createMockOrder(
                        user,
                        course,
                        amount,
                        amountInPaise
                );
            }


            // =================================================
            // ================= RAZORPAY MODE ==================
            // =================================================

            return createRazorpayOrder(
                    user,
                    course,
                    amount,
                    amountInPaise
            );


        } catch (RuntimeException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Unable to create payment order: "
                            + exception.getMessage(),
                    exception
            );
        }
    }


    // =====================================================
    // ================= CREATE MOCK ORDER ==================
    // =====================================================

    private CreateOrderResponse createMockOrder(
            User user,
            Course course,
            BigDecimal amount,
            long amountInPaise
    ) {

        String mockOrderId =
                "mock_order_" +
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "");


        Payment payment =
                Payment.builder()
                        .user(user)
                        .course(course)
                        .amount(amount)
                        .currency("INR")
                        .razorpayOrderId(mockOrderId)
                        .razorpayPaymentId(null)
                        .status("CREATED")
                        .createdAt(LocalDateTime.now())
                        .paidAt(null)
                        .build();


        paymentRepository.save(payment);


        return new CreateOrderResponse(
                mockOrderId,
                "MOCK_KEY",
                course.getId(),
                course.getTitle(),
                amount,
                amountInPaise,
                "INR"
        );
    }


    // =====================================================
    // =============== CREATE RAZORPAY ORDER ===============
    // =====================================================

    private CreateOrderResponse createRazorpayOrder(
            User user,
            Course course,
            BigDecimal amount,
            long amountInPaise
    ) throws Exception {


        validateRazorpayConfiguration();


        RazorpayClient razorpayClient =
                new RazorpayClient(
                        razorpayKeyId,
                        razorpayKeySecret
                );


        JSONObject orderRequest =
                new JSONObject();


        orderRequest.put(
                "amount",
                amountInPaise
        );

        orderRequest.put(
                "currency",
                "INR"
        );


        String receipt =
                "ls_" +
                        user.getId() +
                        "_" +
                        course.getId() +
                        "_" +
                        System.currentTimeMillis();


        orderRequest.put(
                "receipt",
                receipt
        );


        orderRequest.put(
                "partial_payment",
                false
        );


        JSONObject notes =
                new JSONObject();


        notes.put(
                "courseId",
                String.valueOf(course.getId())
        );

        notes.put(
                "userId",
                String.valueOf(user.getId())
        );

        notes.put(
                "userEmail",
                user.getEmail()
        );


        orderRequest.put(
                "notes",
                notes
        );


        Order razorpayOrder =
                razorpayClient
                        .orders
                        .create(orderRequest);


        String razorpayOrderId =
                razorpayOrder.get("id");


        Payment payment =
                Payment.builder()
                        .user(user)
                        .course(course)
                        .amount(amount)
                        .currency("INR")
                        .razorpayOrderId(razorpayOrderId)
                        .razorpayPaymentId(null)
                        .status("CREATED")
                        .createdAt(LocalDateTime.now())
                        .paidAt(null)
                        .build();


        paymentRepository.save(payment);


        return new CreateOrderResponse(
                razorpayOrderId,
                razorpayKeyId,
                course.getId(),
                course.getTitle(),
                amount,
                amountInPaise,
                "INR"
        );
    }


    // =====================================================
    // ================= VERIFY PAYMENT =====================
    // =====================================================

    @Transactional
    public Enrollment verifyPayment(
            String email,
            VerifyPaymentRequest request
    ) {

        try {

            validateVerificationRequest(request);


            // ================= FIND USER =================

            User user =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );


            // ================= FIND PAYMENT =================

            Payment payment =
                    paymentRepository
                            .findByRazorpayOrderId(
                                    request.getRazorpayOrderId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Payment order not found"
                                    )
                            );


            // ================= OWNERSHIP CHECK =================

            if (
                    payment.getUser() == null ||
                            !payment.getUser()
                                    .getId()
                                    .equals(user.getId())
            ) {

                throw new RuntimeException(
                        "This payment order does not belong to you"
                );
            }


            // ================= ALREADY SUCCESS =================

            if ("SUCCESS".equals(payment.getStatus())) {

                return getExistingEnrollment(
                        user,
                        payment
                );
            }


            // =================================================
            // ================= MOCK VERIFICATION ==============
            // =================================================

            if (isMockMode()) {

                return verifyMockPayment(
                        email,
                        user,
                        payment,
                        request
                );
            }


            // =================================================
            // =============== RAZORPAY VERIFICATION ============
            // =================================================

            return verifyRazorpayPayment(
                    email,
                    user,
                    payment,
                    request
            );


        } catch (RuntimeException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Unable to verify payment: "
                            + exception.getMessage(),
                    exception
            );
        }
    }


    // =====================================================
    // ================= VERIFY MOCK ========================
    // =====================================================

    private Enrollment verifyMockPayment(
            String email,
            User user,
            Payment payment,
            VerifyPaymentRequest request
    ) {

        /*
         * MOCK MODE IS DEVELOPMENT ONLY.
         *
         * No real money is transferred.
         */


        if (!payment
                .getRazorpayOrderId()
                .startsWith("mock_order_")) {

            throw new RuntimeException(
                    "Invalid mock payment order"
            );
        }


        String mockPaymentId =
                request.getRazorpayPaymentId();


        if (
                mockPaymentId == null ||
                        mockPaymentId.isBlank()
        ) {

            mockPaymentId =
                    "mock_pay_" +
                            UUID.randomUUID()
                                    .toString()
                                    .replace("-", "");
        }


        payment.setRazorpayPaymentId(
                mockPaymentId
        );

        payment.setStatus(
                "SUCCESS"
        );

        payment.setPaidAt(
                LocalDateTime.now()
        );


        paymentRepository.save(payment);


        return createEnrollmentAfterPayment(
                email,
                user,
                payment
        );
    }


    // =====================================================
    // =============== VERIFY RAZORPAY =====================
    // =====================================================

    private Enrollment verifyRazorpayPayment(
            String email,
            User user,
            Payment payment,
            VerifyPaymentRequest request
    ) throws Exception {


        validateRazorpayConfiguration();


        JSONObject verificationData =
                new JSONObject();


        verificationData.put(
                "razorpay_order_id",
                payment.getRazorpayOrderId()
        );


        verificationData.put(
                "razorpay_payment_id",
                request.getRazorpayPaymentId()
        );


        verificationData.put(
                "razorpay_signature",
                request.getRazorpaySignature()
        );


        boolean signatureValid =
                Utils.verifyPaymentSignature(
                        verificationData,
                        razorpayKeySecret
                );


        if (!signatureValid) {

            payment.setStatus(
                    "FAILED"
            );

            paymentRepository.save(
                    payment
            );


            throw new RuntimeException(
                    "Payment verification failed"
            );
        }


        // ================= DUPLICATE PAYMENT CHECK =================

        paymentRepository
                .findByRazorpayPaymentId(
                        request.getRazorpayPaymentId()
                )
                .ifPresent(existingPayment -> {

                    if (
                            !existingPayment
                                    .getId()
                                    .equals(payment.getId())
                    ) {

                        throw new RuntimeException(
                                "This Razorpay payment has already been used"
                        );
                    }
                });


        payment.setRazorpayPaymentId(
                request.getRazorpayPaymentId()
        );

        payment.setStatus(
                "SUCCESS"
        );

        payment.setPaidAt(
                LocalDateTime.now()
        );


        paymentRepository.save(
                payment
        );


        return createEnrollmentAfterPayment(
                email,
                user,
                payment
        );
    }


    // =====================================================
    // ============== CREATE ENROLLMENT ====================
    // =====================================================

    private Enrollment createEnrollmentAfterPayment(
            String email,
            User user,
            Payment payment
    ) {

        Long courseId =
                payment
                        .getCourse()
                        .getId();


        boolean alreadyEnrolled =
                enrollmentRepository
                        .existsByUserIdAndCourseId(
                                user.getId(),
                                courseId
                        );


        if (alreadyEnrolled) {

            return enrollmentRepository
                    .findByUserIdAndCourseId(
                            user.getId(),
                            courseId
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Enrollment not found"
                            )
                    );
        }


        return enrollmentService
                .enrollStudent(
                        email,
                        courseId
                );
    }


    // =====================================================
    // =============== EXISTING ENROLLMENT =================
    // =====================================================

    private Enrollment getExistingEnrollment(
            User user,
            Payment payment
    ) {

        Long courseId =
                payment
                        .getCourse()
                        .getId();


        return enrollmentRepository
                .findByUserIdAndCourseId(
                        user.getId(),
                        courseId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment was successful but enrollment was not found"
                        )
                );
    }


    // =====================================================
    // ============== VALIDATE VERIFY REQUEST ==============
    // =====================================================

    private void validateVerificationRequest(
            VerifyPaymentRequest request
    ) {

        if (request == null) {

            throw new RuntimeException(
                    "Payment verification request is required"
            );
        }


        if (
                request.getRazorpayOrderId() == null ||
                        request.getRazorpayOrderId().isBlank()
        ) {

            throw new RuntimeException(
                    "Payment order ID is required"
            );
        }


        /*
         * Real Razorpay mode requires all three values.
         *
         * Mock mode only requires order ID.
         */

        if (!isMockMode()) {

            if (
                    request.getRazorpayPaymentId() == null ||
                            request.getRazorpayPaymentId().isBlank()
            ) {

                throw new RuntimeException(
                        "Razorpay payment ID is required"
                );
            }


            if (
                    request.getRazorpaySignature() == null ||
                            request.getRazorpaySignature().isBlank()
            ) {

                throw new RuntimeException(
                        "Razorpay signature is required"
                );
            }
        }
    }


    // =====================================================
    // ================= MODE CHECK =========================
    // =====================================================

    private boolean isMockMode() {

        return "mock"
                .equalsIgnoreCase(
                        paymentMode
                );
    }


    // =====================================================
    // ============ VALIDATE RAZORPAY CONFIG ===============
    // =====================================================

    private void validateRazorpayConfiguration() {

        if (
                razorpayKeyId == null ||
                        razorpayKeyId.isBlank() ||
                        razorpayKeySecret == null ||
                        razorpayKeySecret.isBlank()
        ) {

            throw new RuntimeException(
                    "Razorpay credentials are not configured"
            );
        }
    }
}