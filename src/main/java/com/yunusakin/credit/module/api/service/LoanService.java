package com.yunusakin.credit.module.api.service;

import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.repository.CustomerRepository;
import com.yunusakin.credit.module.api.repository.LoanInstallmentRepository;
import com.yunusakin.credit.module.api.repository.LoanRepository;
import com.yunusakin.credit.module.api.repository.domain.Customer;
import com.yunusakin.credit.module.api.repository.domain.Loan;
import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository installmentRepository;
    private final CustomerRepository customerRepository;

    public LoanService(LoanRepository loanRepository, LoanInstallmentRepository installmentRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.installmentRepository = installmentRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Loan createLoan(LoanDTO loanDTO) {
        Customer customer = customerRepository.findById(loanDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        validateCreditLimit(customer, loanDTO.getLoanAmount());

        BigDecimal totalLoanAmount = loanDTO.getLoanAmount().multiply(BigDecimal.ONE.add(BigDecimal.valueOf(loanDTO.getInterestRate())));
        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(totalLoanAmount.setScale(2, RoundingMode.HALF_EVEN));
        loan.setNumberOfInstallments(Integer.parseInt(loanDTO.getNumberOfInstallments()));
        loan.setIsPaid(false);
        loanRepository.save(loan);

        generateInstallments(loan, totalLoanAmount);
        updateCustomerCreditUsage(customer, loanDTO.getLoanAmount());

        return loan;
    }

    private void validateCreditLimit(Customer customer, BigDecimal loanAmount) {
        if (customer.getUsedCreditLimit().add(loanAmount).compareTo(customer.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Credit limit exceeded.");
        }
    }

    private void updateCustomerCreditUsage(Customer customer, BigDecimal loanAmount) {
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
        customerRepository.save(customer);
    }

    private void generateInstallments(Loan loan, BigDecimal totalLoanAmount) {
        BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(loan.getNumberOfInstallments()), RoundingMode.HALF_EVEN);
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        List<LoanInstallment> installments = IntStream.range(0, loan.getNumberOfInstallments())
                .mapToObj(i -> {
                    LoanInstallment installment = new LoanInstallment();
                    installment.setLoan(loan);
                    installment.setAmount(installmentAmount);
                    installment.setDueDate(dueDate.plusMonths(i));
                    installment.setIsPaid(false);
                    return installment;
                })
                .collect(Collectors.toList());

        installmentRepository.saveAll(installments);
    }

    public List<Loan> listLoans(Long customerId, Boolean isPaid, Integer numberOfInstallments, LocalDate dueDateBefore, BigDecimal remainingBalanceAbove) {
        return loanRepository.findAll().stream()
                .filter(loan -> loan.getCustomer().getId().equals(customerId))
                .filter(loan -> isPaid == null || loan.getIsPaid().equals(isPaid))
                .filter(loan -> numberOfInstallments == null || loan.getNumberOfInstallments().equals(numberOfInstallments))
                .filter(loan -> dueDateBefore == null || loan.getInstallments().stream().anyMatch(installment -> installment.getDueDate().isBefore(dueDateBefore)))
                .filter(loan -> remainingBalanceAbove == null || calculateRemainingBalance(loan).compareTo(remainingBalanceAbove) > 0)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateRemainingBalance(Loan loan) {
        return loan.getInstallments().stream()
                .filter(installment -> !installment.getIsPaid())
                .map(installment -> installment.getAmount().subtract(installment.getPaidAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public String payInstallments(Long loanId, BigDecimal paymentAmount) {
        List<LoanInstallment> installments = installmentRepository.findAll().stream()
                .filter(inst -> inst.getLoan().getId().equals(loanId)
                        && !inst.getIsPaid()
                        && inst.getDueDate().isBefore(LocalDate.now().plusMonths(3)))
                .sorted((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                .collect(Collectors.toList());

        BigDecimal remainingPayment = paymentAmount;
        int installmentsPaid = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (LoanInstallment installment : installments) {
            BigDecimal remainingInstallmentAmount = installment.getAmount().subtract(installment.getPaidAmount());
            if (remainingPayment.compareTo(remainingInstallmentAmount) >= 0) {
                remainingPayment = remainingPayment.subtract(remainingInstallmentAmount);
                totalPaid = totalPaid.add(remainingInstallmentAmount);
                installment.setPaidAmount(installment.getAmount());
                installment.setIsPaid(true);
                installmentsPaid++;
            } else {
                installment.setPaidAmount(installment.getPaidAmount().add(remainingPayment));
                totalPaid = totalPaid.add(remainingPayment);
                remainingPayment = BigDecimal.ZERO;
                break;
            }
        }
        installmentRepository.saveAll(installments);

        updateLoanStatus(loanId, installments);

        return String.format("Installments Paid: %d, Total Paid: %.2f, Remaining Payment: %.2f", installmentsPaid, totalPaid, remainingPayment);
    }

    private void updateLoanStatus(Long loanId, List<LoanInstallment> installments) {
        boolean allPaid = installments.stream().allMatch(LoanInstallment::getIsPaid);
        if (allPaid) {
            Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
            loan.setIsPaid(true);
            loanRepository.save(loan);
        }
    }

    private BigDecimal applyRewardOrPenalty(LoanInstallment installment) {
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), installment.getDueDate());
        BigDecimal adjustmentFactor = installment.getAmount().multiply(BigDecimal.valueOf(0.001 * Math.abs(daysDifference)));

        return installment.getAmount().add(adjustmentFactor.multiply(BigDecimal.valueOf(-Math.signum(daysDifference))));
    }

}
