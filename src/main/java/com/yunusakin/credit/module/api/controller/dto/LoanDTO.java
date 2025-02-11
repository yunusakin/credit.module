package com.yunusakin.credit.module.api.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;

@Data
public class LoanDTO implements Serializable {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @Positive(message = "Loan amount must be positive")
    private BigDecimal loanAmount;

    @NotNull(message = "Number of installments is required")
    @Pattern(regexp = "6|9|12|24", message = "Number of installments must be 6, 9, 12, or 24")
    private String numberOfInstallments;

    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must not exceed 0.5")
    private double interestRate;
}
