package com.yunusakin.credit.module.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayInstallmentDTO implements Serializable {
    @JsonProperty("loanId")
    @NotNull
    private Long loanId;

    @JsonProperty("paymentAmount")
    @NotNull
    private BigDecimal paymentAmount;
}
