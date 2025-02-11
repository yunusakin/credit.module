package com.yunusakin.credit.module.api.repository.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yunusakin.credit.module.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LoanInstallment extends BaseEntity {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private BigDecimal amount;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private LocalDate dueDate;
    private LocalDateTime paymentDate;
    private Boolean isPaid = false;
}
