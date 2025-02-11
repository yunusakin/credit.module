package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.mapper.EntityMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/loans")
@Tag(name = "Loan API", description = "Operations related to loans")
public class LoanController {
    private final LoanService loanService;
    private final EntityMapper entityMapper;

    public LoanController(LoanService loanService, EntityMapper entityMapper) {
        this.loanService = loanService;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "Create a new loan", description = "Creates a loan for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid loan details provided")
    })
    @PostMapping
    public ResponseEntity<BaseApiResponse<LoanDTO>> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        var createdLoan = loanService.createLoan(loanDTO);
        var responseDTO = entityMapper.toLoanDTO(createdLoan);
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Loan created successfully", responseDTO));
    }

    @Operation(summary = "List loans for a customer", description = "Retrieve loans associated with a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<BaseApiResponse<List<LoanDTO>>> listLoans(
            @PathVariable Long customerId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Integer numberOfInstallments,
            @RequestParam(required = false) LocalDate dueDateBefore,
            @RequestParam(required = false) BigDecimal remainingBalanceAbove) {

        List<LoanDTO> loans = loanService.listLoans(customerId, isPaid, numberOfInstallments, dueDateBefore, remainingBalanceAbove)
                .stream().map(entityMapper::toLoanDTO).collect(Collectors.toList());

        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Loans retrieved successfully", loans));
    }
}
