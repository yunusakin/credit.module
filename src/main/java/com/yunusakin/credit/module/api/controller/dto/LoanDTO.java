package com.yunusakin.credit.module.api.controller.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LoanDTO implements Serializable {

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @Positive(message = "Loan amount must be positive")
    @NotNull
    private BigDecimal loanAmount;

    @NotNull(message = "Number of installments is required")
    @Pattern(regexp = "6|9|12|24", message = "Number of installments must be 6, 9, 12, or 24")
    private String numberOfInstallments;


    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must not exceed 0.5")
    private double interestRate;
}
