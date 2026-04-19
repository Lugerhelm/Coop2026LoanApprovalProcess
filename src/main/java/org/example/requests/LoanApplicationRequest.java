package org.example.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

// Taotluse info DTO
@Data
public class LoanApplicationRequest {
    @NotBlank(message = "First name is mandatory")
    @Size(max = 32, message="First name cannot exceed 32 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 32, message="Last name cannot exceed 32 characters")
    private String lastName;

    @NotBlank(message = "Personal code is mandatory")
    @Size(min = 11, max = 11, message="Personal code must be 11 digits long")
    private String personalCode;

    @NotNull(message = "Loan period must be entered")
    @Min(value = 6, message = "Minimum loan period is 6 months")
    @Max(value = 360, message = "Maximum loan period is 360 months")
    private Integer loanPeriodMonths;

    @NotNull(message = "Interest margin is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest margin must be 0 or greater")
    private BigDecimal interestMargin;

    @NotNull(message = "Interest rate (Euribor) is mandatory")
    private BigDecimal baseInterestRate;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "5000.0", inclusive = true, message = "Amount must be 5000 or greater")
    private BigDecimal amount;
}
