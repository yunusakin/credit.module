package com.yunusakin.credit.module.api.service;

import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.repository.CustomerRepository;
import com.yunusakin.credit.module.api.repository.LoanInstallmentRepository;
import com.yunusakin.credit.module.api.repository.LoanRepository;
import com.yunusakin.credit.module.api.repository.domain.Customer;
import com.yunusakin.credit.module.api.repository.domain.Loan;
import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository installmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLoan_ShouldCreateLoanSuccessfully() {
        Customer customer = createCustomer(1L, "10000", "2000");

        LoanDTO loanDTO = createLoanDTO(1L, "5000", 0.1, "12");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Loan loan = loanService.createLoan(loanDTO);

        assertNotNull(loan);
        assertEquals(new BigDecimal("5500.00"), loan.getLoanAmount());
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(installmentRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createLoan_ShouldThrowException_WhenCreditLimitExceeded() {
        Customer customer = createCustomer(1L, "10000", "9500");

        LoanDTO loanDTO = createLoanDTO(1L, "1000", 0.1, "12");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("Credit limit exceeded.", exception.getMessage());
    }

    @Test
    void payInstallments_ShouldPayAllInstallments_WhenPaymentIsEnough() {
        Loan loan = createLoan(1L);
        LoanInstallment installment1 = createInstallment("500", "0", false, LocalDate.now().minusMonths(1), loan);
        LoanInstallment installment2 = createInstallment("500", "0", false, LocalDate.now(), loan);

        when(installmentRepository.findAll()).thenReturn(Arrays.asList(installment1, installment2));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("1000"));

        assertEquals("Installments Paid: 2, Total Paid: 1000.00, Remaining Payment: 0.00", result);
        assertTrue(installment1.getIsPaid());
        assertTrue(installment2.getIsPaid());
    }

    @Test
    void payInstallments_ShouldHandlePartialPaymentCorrectly() {
        Loan loan = createLoan(1L);
        LoanInstallment installment1 = createInstallment("500", "0", false, LocalDate.now(), loan);
        LoanInstallment installment2 = createInstallment("500", "0", false, LocalDate.now().plusMonths(1), loan);

        when(installmentRepository.findAll()).thenReturn(Arrays.asList(installment1, installment2));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("750"));

        assertEquals("Installments Paid: 1, Total Paid: 750.00, Remaining Payment: 0.00", result);
        assertTrue(installment1.getIsPaid());
        assertFalse(installment2.getIsPaid());
    }

    @Test
    void payInstallments_ShouldNotPayInstallmentsBeyond3Months() {
        Loan loan = createLoan(1L);
        LoanInstallment installment1 = createInstallment("500", "0", false, LocalDate.now(), loan);
        LoanInstallment installment2 = createInstallment("500", "0", false, LocalDate.now().plusMonths(4), loan);

        when(installmentRepository.findAll()).thenReturn(Arrays.asList(installment1, installment2));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("500"));

        assertEquals("Installments Paid: 1, Total Paid: 500.00, Remaining Payment: 0.00", result);
        assertTrue(installment1.getIsPaid());
        assertFalse(installment2.getIsPaid());
    }

    @Test
    void payInstallments_ShouldHandleOverpayment() {
        Loan loan = createLoan(1L);
        LoanInstallment installment1 = createInstallment("500", "0", false, LocalDate.now(), loan);
        LoanInstallment installment2 = createInstallment("500", "0", false, LocalDate.now(), loan);

        when(installmentRepository.findAll()).thenReturn(Arrays.asList(installment1, installment2));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("1500"));

        assertEquals("Installments Paid: 2, Total Paid: 1000.00, Remaining Payment: 500.00", result);
    }

    @Test
    void payInstallments_ShouldThrowException_WhenLoanNotFound() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> loanService.payInstallments(99L, new BigDecimal("500")));
        assertEquals("Loan not found", exception.getMessage());
    }

    @Test
    void payInstallments_ShouldHandleNoInstallmentsCorrectly() {
        Loan loan = createLoan(1L);

        when(installmentRepository.findAll()).thenReturn(Collections.emptyList());
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("500"));

        assertEquals("Installments Paid: 0, Total Paid: 0.00, Remaining Payment: 500.00", result);
    }

    @Test
    void createLoan_ShouldThrowException_WhenCustomerNotFound() {
        LoanDTO loanDTO = createLoanDTO(999L, "5000", 0.1, "12");

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void payInstallments_ShouldNotPayWhenNoDueInstallments() {
        Loan loan = createLoan(1L);
        LoanInstallment paidInstallment = createInstallment("500", "500", true, LocalDate.now(), loan);

        when(installmentRepository.findAll()).thenReturn(Collections.singletonList(paidInstallment));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        String result = loanService.payInstallments(1L, new BigDecimal("500"));

        assertEquals("Installments Paid: 0, Total Paid: 0.00, Remaining Payment: 500.00", result);
    }

    // Utility methods for creating test objects
    private Customer createCustomer(Long id, String creditLimit, String usedCreditLimit) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setCreditLimit(new BigDecimal(creditLimit));
        customer.setUsedCreditLimit(new BigDecimal(usedCreditLimit));
        return customer;
    }

    private Loan createLoan(Long id) {
        Loan loan = new Loan();
        loan.setId(id);
        return loan;
    }

    private LoanDTO createLoanDTO(Long customerId, String loanAmount, double interestRate, String numberOfInstallments) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setCustomerId(customerId);
        loanDTO.setLoanAmount(new BigDecimal(loanAmount));
        loanDTO.setInterestRate(interestRate);
        loanDTO.setNumberOfInstallments(numberOfInstallments);
        return loanDTO;
    }

    private LoanInstallment createInstallment(String amount, String paidAmount, boolean isPaid, LocalDate dueDate, Loan loan) {
        LoanInstallment installment = new LoanInstallment();
        installment.setLoan(loan);
        installment.setAmount(new BigDecimal(amount));
        installment.setPaidAmount(new BigDecimal(paidAmount));
        installment.setIsPaid(isPaid);
        installment.setDueDate(dueDate);
        return installment;
    }
}