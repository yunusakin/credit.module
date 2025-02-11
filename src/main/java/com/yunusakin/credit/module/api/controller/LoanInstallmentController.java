package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.LoanInstallmentDTO;
import com.yunusakin.credit.module.api.controller.dto.PayInstallmentDTO;
import com.yunusakin.credit.module.api.mapper.EntityMapper;
import com.yunusakin.credit.module.api.service.LoanInstallmentService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/installments")
@Tag(name = "Installment API", description = "Operations related to loan installments")
public class LoanInstallmentController {
    private final LoanInstallmentService installmentService;
    private final LoanService loanService;
    private final EntityMapper entityMapper;

    public LoanInstallmentController(LoanInstallmentService installmentService, LoanService loanService, EntityMapper entityMapper) {
        this.installmentService = installmentService;
        this.loanService = loanService;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "List all installments for a loan", description = "Retrieve installments associated with a given loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of installments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @GetMapping("/{loanId}")
    public ResponseEntity<BaseApiResponse<List<LoanInstallmentDTO>>> listInstallments(@PathVariable Long loanId) {
        List<LoanInstallmentDTO> installments = installmentService.findInstallmentsByLoanId(loanId).stream()
                .map(entityMapper::toInstallmentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Installments retrieved successfully", installments));
    }

    @Operation(summary = "Pay loan installments", description = "Make a payment towards loan installments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installments paid successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PostMapping("/pay")
    public ResponseEntity<BaseApiResponse<String>> payInstallments(@Valid PayInstallmentDTO payInstallmentDTO) {
        String result = loanService.payInstallments(payInstallmentDTO.getLoanId(), payInstallmentDTO.getPaymentAmount());
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, result, null));
    }
}
