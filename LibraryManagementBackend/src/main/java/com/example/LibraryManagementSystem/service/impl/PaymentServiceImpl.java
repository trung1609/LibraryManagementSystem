package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.PaymentGateway;
import com.example.LibraryManagementSystem.domain.PaymentStatus;
import com.example.LibraryManagementSystem.event.publisher.PaymentEventPublisher;
import com.example.LibraryManagementSystem.exception.PaymentException;
import com.example.LibraryManagementSystem.mapper.PaymentMapper;
import com.example.LibraryManagementSystem.model.Payment;
import com.example.LibraryManagementSystem.model.Subscription;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.PaymentDTO;
import com.example.LibraryManagementSystem.payload.request.PaymentInitiateRequest;
import com.example.LibraryManagementSystem.payload.request.PaymentVerifyRequest;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.example.LibraryManagementSystem.payload.response.PaymentLinkResponse;
import com.example.LibraryManagementSystem.repository.PaymentRepository;
import com.example.LibraryManagementSystem.repository.SubscriptionRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.service.PaymentService;
import com.example.LibraryManagementSystem.service.gateway.RazorpayService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RazorpayService razorpayService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws RazorpayException {

        Users users =  userRepository.findById(request.getUserId()).get();

        Payment payment = new Payment();
        payment.setUsers(users);
        payment.setPaymentType(request.getPaymentType());
        payment.setGateway(request.getGateway());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN_"+ UUID.randomUUID());
        payment.setInitiatedAt(LocalDateTime.now());

        if (request.getSubscriptionId() != null) {
            Subscription sub = subscriptionRepository
                    .findById(request.getSubscriptionId())
                    .orElseThrow(() -> new PaymentException("Subscription not found with id: " + request.getSubscriptionId()));
            payment.setSubscription(sub);
        }
        payment = paymentRepository.save(payment);

        PaymentInitiateResponse response = new PaymentInitiateResponse();
        if (request.getGateway() == PaymentGateway.RAZORPAY){
            PaymentLinkResponse paymentLinkResponse = razorpayService.createPaymentLink(users, payment);
            response = PaymentInitiateResponse.builder()
                    .paymentId(payment.getId())
                    .gateway(request.getGateway())
                    .checkoutUrl(paymentLinkResponse.getPaymentLinkUrl())
                    .transactionId(paymentLinkResponse.getPaymentLinkId())
                    .amount(payment.getAmount())
                    .description(payment.getDescription())
                    .success(true)
                    .message("Payment initiated successfully")
                    .build();

            payment.setGatewayOrderId(paymentLinkResponse.getPaymentLinkId());
        }
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);
        //payment initiate event
        return response;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest request) throws RazorpayException {

        JSONObject paymentDetails = razorpayService.fetchPaymentDetails(request.getRazorpayPaymentId());

        JSONObject notes = paymentDetails.getJSONObject("notes");

        Long paymentId = Long.parseLong(notes.optString("payment_id"));

        Payment payment = paymentRepository.findById(paymentId).get();
        boolean isValid = razorpayService.isValidPayment(request.getRazorpayPaymentId());
        if (PaymentGateway.RAZORPAY == payment.getGateway()) {
            if (isValid) {
                payment.setGatewayOrderId(request.getRazorpayPaymentId());
            }
        }
        if (isValid) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);

            paymentEventPublisher.publishPaymentSuccessEvent(payment);
        }
        return paymentMapper.toDTO(payment);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);

        return payments.map(paymentMapper::toDTO);
    }
}
