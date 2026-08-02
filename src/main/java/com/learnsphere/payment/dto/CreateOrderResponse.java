package com.learnsphere.payment.dto;

import java.math.BigDecimal;

public class CreateOrderResponse {

    private String razorpayOrderId;

    private String razorpayKeyId;

    private Long courseId;

    private String courseTitle;

    private BigDecimal amount;

    private Long amountInPaise;

    private String currency;


    // ================= EMPTY CONSTRUCTOR =================

    public CreateOrderResponse() {
    }


    // ================= CONSTRUCTOR =================

    public CreateOrderResponse(
            String razorpayOrderId,
            String razorpayKeyId,
            Long courseId,
            String courseTitle,
            BigDecimal amount,
            Long amountInPaise,
            String currency
    ) {

        this.razorpayOrderId = razorpayOrderId;
        this.razorpayKeyId = razorpayKeyId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.amount = amount;
        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }


    // ================= GETTERS / SETTERS =================

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(
            String razorpayOrderId
    ) {
        this.razorpayOrderId = razorpayOrderId;
    }


    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public void setRazorpayKeyId(
            String razorpayKeyId
    ) {
        this.razorpayKeyId = razorpayKeyId;
    }


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }


    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(
            String courseTitle
    ) {
        this.courseTitle = courseTitle;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Long getAmountInPaise() {
        return amountInPaise;
    }

    public void setAmountInPaise(
            Long amountInPaise
    ) {
        this.amountInPaise = amountInPaise;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
