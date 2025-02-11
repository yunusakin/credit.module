package com.yunusakin.credit.module.api.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayInstallmentDTO implements Serializable {
    private Long loanId;
    private BigDecimal paymentAmount;
}
