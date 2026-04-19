package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.repository.PaymentScheduleRepository;
import org.example.table.LoanApplication;
import org.example.repository.LoanApplicationRepository;
import org.example.table.PaymentSchedule;
import org.example.util.PersonalCodeUtil;
import org.example.util.Status;
import org.example.requests.LoanApplicationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    // Repod
    private final LoanApplicationRepository repository;
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Value("${loan.limits.max-age}")
    private int maxAgeLimit;

    // Loome uue laenutaotluse, salvestame selle ning liigume edasi vanuse ja isikukoodi kontrolli
    @Transactional
    public LoanApplication createApplication(LoanApplicationRequest request) {
        List<Status> activeStatuses = List.of(Status.STARTED, Status.IN_REVIEW);
        if (repository.existsByPersonalCodeAndStatusIn(request.getPersonalCode(), activeStatuses)) {
            throw new IllegalStateException("Customer already has an active loan application.");
        }
        LoanApplication newApp = new LoanApplication();
        newApp.setFirstName(request.getFirstName());
        newApp.setLastName(request.getLastName());
        newApp.setPersonalCode(request.getPersonalCode());
        newApp.setLoanPeriodMonths(request.getLoanPeriodMonths());
        newApp.setInterestMargin(request.getInterestMargin());
        newApp.setBaseInterestRate(request.getBaseInterestRate());
        newApp.setAmount(request.getAmount());

        newApp.setStatus(Status.STARTED);

        newApp = repository.save(newApp);

        return processApplication(newApp);
    }

    // Vanuse ja isikukoodi kontroll, läbimisel loome graafiku
    @Transactional
    public LoanApplication processApplication(LoanApplication application) {
        application.setStatus(Status.STARTED);
        if (!PersonalCodeUtil.mod11check(application.getPersonalCode())) {
            application.setStatus(Status.REJECTED);
            application.setRejectionReason("FAILED_MOD11_CHECK");
            return repository.save(application);
        }
        LocalDate birthDate = PersonalCodeUtil.extractBirthDate(application.getPersonalCode());
        int age = PersonalCodeUtil.calculateAge(birthDate);
        if (age > maxAgeLimit) {
            application.setStatus(Status.REJECTED);
            application.setRejectionReason("CUSTOMER_TOO_OLD");
            return repository.save(application);
        }

        generateSchedule(application);

        application.setStatus(Status.IN_REVIEW);
        return repository.save(application);

    }

    // IN_REVIEW taotluse staatuse muutmine
    @Transactional
    public LoanApplication updateStatus(Long id, Status status, String reason) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (app.getStatus() != Status.IN_REVIEW) {
            throw new IllegalStateException("Only applications in review can be finalized.");
        }
        if (status == Status.IN_REVIEW || status == Status.STARTED) {
            throw new IllegalStateException("IN_REVIEW application can be set only to REJECTED or APPROVED.");
        }
        app.setStatus(status);
        if (status == Status.REJECTED) {
            app.setRejectionReason(reason != null ? reason : "MANUALLY_REJECTED");
        }

        return repository.save(app);
    }

    // Annuiteedigraafiku loomine, arvutamine ja andmebaasi salvestamine
    public void generateSchedule(LoanApplication application) {
        Integer loanPeriodMonths = application.getLoanPeriodMonths();
        if (loanPeriodMonths == null || loanPeriodMonths <= 0) {
            throw new IllegalArgumentException("Loan period months must be greater than 0.");
        }
        BigDecimal interestMargin = application.getInterestMargin();
        BigDecimal baseInterestRate = application.getBaseInterestRate();
        BigDecimal annualRate = (interestMargin != null ? interestMargin : BigDecimal.ZERO)
                .add(baseInterestRate != null ? baseInterestRate : BigDecimal.ZERO);

        if (annualRate.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Annual rate must be greater than 0.");
        }
        double monthlyRate = annualRate.divide(BigDecimal.valueOf(12),10,  RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .doubleValue();
        double loanAmount = application.getAmount().doubleValue();

        BigDecimal monthlyPayment = BigDecimal.valueOf(
                (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -loanPeriodMonths))
        ).setScale(2, RoundingMode.HALF_UP);

        LocalDate paymentDate = LocalDate.now();

        for (int i = 1; i <= loanPeriodMonths; i++) {
            PaymentSchedule scheduleEntry = new PaymentSchedule();
            scheduleEntry.setMonthlyPayment(monthlyPayment);
            scheduleEntry.setPaymentDate(paymentDate);
            scheduleEntry.setLoanApplication(application);
            scheduleEntry.setPaymentNumber(i);

            paymentScheduleRepository.save(scheduleEntry);

            paymentDate = paymentDate.plusMonths(1);
        }

    }

    // Saame kõik taotlused
    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }
}
