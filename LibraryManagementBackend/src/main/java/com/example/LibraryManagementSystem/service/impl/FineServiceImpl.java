package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import com.example.LibraryManagementSystem.domain.PaymentGateway;
import com.example.LibraryManagementSystem.domain.PaymentType;
import com.example.LibraryManagementSystem.mapper.FineMapper;
import com.example.LibraryManagementSystem.model.BookLoan;
import com.example.LibraryManagementSystem.model.Fine;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.FineDTO;
import com.example.LibraryManagementSystem.payload.request.CreateFineRequest;
import com.example.LibraryManagementSystem.payload.request.PaymentInitiateRequest;
import com.example.LibraryManagementSystem.payload.request.WaiveFineRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.example.LibraryManagementSystem.repository.BookLoanRepository;
import com.example.LibraryManagementSystem.repository.FineRepository;
import com.example.LibraryManagementSystem.service.FineService;
import com.example.LibraryManagementSystem.service.PaymentService;
import com.example.LibraryManagementSystem.service.UserService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final BookLoanRepository bookLoanRepository;
    private final FineMapper fineMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public FineDTO createFine(CreateFineRequest request) {
        //1. Validate book loan exists
        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan not found with id: " + request.getBookLoanId()));

        //2. create fine

        Fine fine = Fine.builder()
                .bookLoan(bookLoan)
                .users(bookLoan.getUsers())
                .type(request.getType())
                .amount(request.getAmount())
                .status(FineStatus.PENDING)
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();
        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public PaymentInitiateResponse payFine(Long fineId, String transactionId) throws RazorpayException {
        //1. Validate fine exists
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found with id: " + fineId));

        //2. check already paid
        if (fine.getStatus().equals(FineStatus.PAID)) {
            throw new RuntimeException("Fine is already paid");
        }

        if (fine.getStatus().equals(FineStatus.WAIVED)) {
            throw new RuntimeException("Fine is already waived");
        }

        //3. Initiate payment
        Users users = userService.getCurrentUser();

        PaymentInitiateRequest request = PaymentInitiateRequest.builder()
                .userId(users.getId())
                .fineId(fine.getId())
                .paymentType(PaymentType.FINE)
                .gateway(PaymentGateway.RAZORPAY)
                .amount(fine.getAmount())
                .description("Library Fine Payment")
                .build();
        return paymentService.initiatePayment(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) {

    }

    @Override
    public FineDTO waiveFine(WaiveFineRequest request) {
        return null;
    }

    @Override
    public List<FineDTO> getMyFines(FineStatus status, FineType type) {
        return List.of();
    }

    @Override
    public PageResponse<FineDTO> getAllFines(FineStatus status, FineType type, Long userId, int page, int size) {
        return null;
    }
}
