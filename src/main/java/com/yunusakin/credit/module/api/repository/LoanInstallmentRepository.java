package com.yunusakin.credit.module.api.repository;

import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanId(Long loanId);
}
