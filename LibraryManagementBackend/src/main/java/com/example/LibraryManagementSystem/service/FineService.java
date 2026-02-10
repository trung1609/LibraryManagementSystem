package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import com.example.LibraryManagementSystem.payload.dto.FineDTO;
import com.example.LibraryManagementSystem.payload.request.CreateFineRequest;
import com.example.LibraryManagementSystem.payload.request.WaiveFineRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.razorpay.RazorpayException;

import java.util.List;

public interface FineService {
    FineDTO createFine(CreateFineRequest request);

    PaymentInitiateResponse payFine(Long fineId, String transactionId) throws RazorpayException;

    void markFineAsPaid(Long fineId, Long amount, String transactionId);

    FineDTO waiveFine(WaiveFineRequest request);

    List<FineDTO> getMyFines(FineStatus status, FineType type);

    PageResponse<FineDTO> getAllFines(
            FineStatus status, FineType type, Long userId, int page, int size
    );

}
