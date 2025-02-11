package com.yunusakin.credit.module.api.repository;

import com.yunusakin.credit.module.api.repository.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(Long customerId);
}