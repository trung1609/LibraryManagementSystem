package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.PaymentDTO;
import com.example.LibraryManagementSystem.payload.request.PaymentInitiateRequest;
import com.example.LibraryManagementSystem.payload.request.PaymentVerifyRequest;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.razorpay.RazorpayException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws RazorpayException;
    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws RazorpayException;
    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
