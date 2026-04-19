package org.example.table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
// Annuiteedigraafiku info
@Entity
@Getter
@Setter
@Table(name = "payment_schedules")
public class PaymentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    private Integer paymentNumber;
    private LocalDate paymentDate;
    private BigDecimal monthlyPayment;



}
