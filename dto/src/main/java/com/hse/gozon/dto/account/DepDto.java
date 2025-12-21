package com.hse.gozon.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class DepDto {
    @NotNull
    private Integer accountId;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
