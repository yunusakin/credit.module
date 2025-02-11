package com.yunusakin.credit.module.api.service;

import com.yunusakin.credit.module.api.repository.LoanInstallmentRepository;
import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanInstallmentService {
    private final LoanInstallmentRepository installmentRepository;

    public LoanInstallmentService(LoanInstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
    }

    public List<LoanInstallment> findInstallmentsByLoanId(Long loanId) {
        return installmentRepository.findAll().stream()
                .filter(installment -> installment.getLoan().getId().equals(loanId))
                .toList();
    }
}
