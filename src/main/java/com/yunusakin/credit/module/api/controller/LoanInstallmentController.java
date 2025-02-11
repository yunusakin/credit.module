package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.LoanInstallmentDTO;
import com.yunusakin.credit.module.api.controller.dto.PayInstallmentDTO;
import com.yunusakin.credit.module.api.mapper.EntityMapper;
import com.yunusakin.credit.module.api.service.LoanInstallmentService;
import com.yunusakin.credit.module.api.service.LoanService;
import com.yunusakin.credit.module.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/installments")
public class LoanInstallmentController {
    private final LoanInstallmentService installmentService;
    private final LoanService loanService;
    private final EntityMapper entityMapper;

    public LoanInstallmentController(LoanInstallmentService installmentService, LoanService loanService, EntityMapper entityMapper) {
        this.installmentService = installmentService;
        this.loanService = loanService;
        this.entityMapper = entityMapper;
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<ApiResponse<List<LoanInstallmentDTO>>> listInstallments(@PathVariable Long loanId) {
        List<LoanInstallmentDTO> installments = installmentService.findInstallmentsByLoanId(loanId).stream()
                .map(entityMapper::toInstallmentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Installments retrieved successfully", installments));
    }

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<String>> payInstallments(@Valid PayInstallmentDTO payInstallmentDTO) {
        String result = loanService.payInstallments(payInstallmentDTO.getLoanId(), payInstallmentDTO.getPaymentAmount());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, result, null));
    }
}
