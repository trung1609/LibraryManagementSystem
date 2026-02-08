package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import com.example.LibraryManagementSystem.payload.dto.FineDTO;
import com.example.LibraryManagementSystem.payload.request.CreateFineRequest;
import com.example.LibraryManagementSystem.payload.request.WaiveFineRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.example.LibraryManagementSystem.service.FineService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {
    private final FineService fineService;

    @PostMapping
    public ResponseEntity<?> createFine(@Valid @RequestBody CreateFineRequest request) {
        FineDTO fineDTO = fineService.createFine(request);
        return ResponseEntity.ok(fineDTO);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id, @RequestParam(required = false) String transactionId) throws RazorpayException {
        PaymentInitiateResponse payFine = fineService.payFine(id, transactionId);
        return ResponseEntity.ok(payFine);
    }

    @PostMapping("/waive")
    public ResponseEntity<?> waiveFine(@Valid @RequestBody WaiveFineRequest request) {
        FineDTO fineDTO = fineService.waiveFine(request);
        return ResponseEntity.ok(fineDTO);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type
    ) {
        List<FineDTO> fines = fineService.getMyFines(status, type);
        return ResponseEntity.ok(fines);
    }

    @GetMapping
    public ResponseEntity<?> getAllFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<FineDTO> response = fineService.getAllFines(status, type, userId, page, size);
        return ResponseEntity.ok(response);
    }
}
