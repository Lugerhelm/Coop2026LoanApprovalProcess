package org.example.table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.util.Status;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "loan_applications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String firstName;

    @Column(length = 32, nullable = false)
    private String lastName;

    @Column(length = 11, nullable = false)
    private String personalCode;

    private Integer loanPeriodMonths;
    private BigDecimal interestMargin;
    private BigDecimal baseInterestRate;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String rejectionReason;
}
