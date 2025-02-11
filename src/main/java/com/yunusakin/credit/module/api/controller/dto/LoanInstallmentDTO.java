package com.yunusakin.credit.module.api.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LoanInstallmentDTO implements Serializable {
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private boolean isPaid;
}
