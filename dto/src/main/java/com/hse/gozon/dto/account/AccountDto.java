package com.hse.gozon.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDto {
    private String name;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal balance;
}
