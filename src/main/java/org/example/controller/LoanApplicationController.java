package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.repository.PaymentScheduleRepository;
import org.example.requests.LoanApplicationRequest;
import org.example.table.LoanApplication;
import org.example.table.PaymentSchedule;
import org.example.service.LoanApplicationService;
import org.example.util.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loan Application API", description = "Endpoints for submitting and managing loan applications")
public class LoanApplicationController {

    private final LoanApplicationService loanService;
    private final PaymentScheduleRepository scheduleRepository;

    @PostMapping("/apply")
    @Operation(summary = "Submit a new loan application")
    public ResponseEntity<LoanApplication> apply(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplication application = loanService.createApplication(request);
        return ResponseEntity.ok(application);
    }

    @GetMapping
    @Operation(summary = "Get all loan applications (for Bank Agents)")
    public List<LoanApplication> getAllApplications() {
        return loanService.getAllApplications();
    }

    @GetMapping("/{id}/schedule")
    @Operation(summary = "Get the payment schedule for a specific application")
    public List<PaymentSchedule> getSchedule(@PathVariable Long id) {
        return scheduleRepository.findByLoanApplicationIdOrderByPaymentNumberAsc(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update application status (Approve or Reject)")
    public ResponseEntity<LoanApplication> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            @RequestParam(required = false) String reason) {

        LoanApplication updated = loanService.updateStatus(id, status, reason);
        return ResponseEntity.ok(updated);
    }
}