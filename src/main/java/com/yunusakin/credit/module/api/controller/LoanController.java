package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.mapper.EntityMapper;
import com.yunusakin.credit.module.api.service.LoanService;
import com.yunusakin.credit.module.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;
    private final EntityMapper entityMapper;

    public LoanController(LoanService loanService, EntityMapper entityMapper) {
        this.loanService = loanService;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoanDTO>> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        var createdLoan = loanService.createLoan(loanDTO);
        var responseDTO = entityMapper.toLoanDTO(createdLoan);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Loan created successfully", responseDTO));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<List<LoanDTO>>> listLoans(
            @PathVariable Long customerId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Integer numberOfInstallments,
            @RequestParam(required = false) LocalDate dueDateBefore,
            @RequestParam(required = false) BigDecimal remainingBalanceAbove) {

        List<LoanDTO> loans = loanService.listLoans(customerId, isPaid, numberOfInstallments, dueDateBefore, remainingBalanceAbove)
                .stream().map(entityMapper::toLoanDTO).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Loans retrieved successfully", loans));
    }
}
