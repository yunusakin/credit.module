package com.yunusakin.credit.module.api.repository.domain;

import com.yunusakin.credit.module.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Loan extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal loanAmount;
    private Integer numberOfInstallments;
    private Boolean isPaid = false;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanInstallment> installments;
}
