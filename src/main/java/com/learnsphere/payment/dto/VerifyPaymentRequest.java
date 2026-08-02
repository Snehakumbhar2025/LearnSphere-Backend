package com.learnsphere.payment.dto;

public class VerifyPaymentRequest {

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;


    // ================= EMPTY CONSTRUCTOR =================

    public VerifyPaymentRequest() {
    }


    // ================= CONSTRUCTOR =================

    public VerifyPaymentRequest(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) {

        this.razorpayOrderId = razorpayOrderId;
        this.razorpayPaymentId = razorpayPaymentId;
        this.razorpaySignature = razorpaySignature;
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


    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(
            String razorpayPaymentId
    ) {
        this.razorpayPaymentId = razorpayPaymentId;
    }


    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(
            String razorpaySignature
    ) {
        this.razorpaySignature = razorpaySignature;
    }
}