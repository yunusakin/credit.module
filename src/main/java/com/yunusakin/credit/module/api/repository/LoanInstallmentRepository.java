package com.yunusakin.credit.module.api.repository;

import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanId(Long loanId);
    @Query("SELECT i FROM LoanInstallment i WHERE i.loan.id = :loanId AND i.isPaid = false AND i.dueDate <= :maxDueDate ORDER BY i.dueDate ASC")
    List<LoanInstallment> findDueInstallments(@Param("loanId") Long loanId, @Param("maxDueDate") LocalDate maxDueDate);

}
