package com.hse.gozon.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccountCreateDto {
    @Size(max = 50)
    private String name;
    @Email
    private String email;
}
