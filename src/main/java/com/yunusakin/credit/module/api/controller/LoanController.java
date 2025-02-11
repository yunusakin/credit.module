package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.repository.domain.Loan;
import com.yunusakin.credit.module.api.service.LoanService;
import com.yunusakin.credit.module.common.response.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/loans")
@Tag(name = "Loan API", description = "Operations related to loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Create a new loan", description = "Creates a loan for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid loan details provided")
    })
    public ResponseEntity<BaseApiResponse<Loan>> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Loan created successfully", loanService.createLoan(loanDTO)));
    }

    @Operation(summary = "List loans for a customer", description = "Retrieve loans associated with a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<BaseApiResponse<List<Loan>>> listLoans(
            @PathVariable Long customerId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Integer numberOfInstallments,
            @RequestParam(required = false) LocalDate dueDateBefore,
            @RequestParam(required = false) BigDecimal remainingBalanceAbove) {

        List<Loan> loans = loanService.listLoans(customerId, isPaid, numberOfInstallments, dueDateBefore, remainingBalanceAbove);

        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Loans retrieved successfully", loans));
    }
}
