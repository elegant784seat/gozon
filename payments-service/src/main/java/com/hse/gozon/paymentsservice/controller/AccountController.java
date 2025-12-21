package com.hse.gozon.paymentsservice.controller;

import com.hse.gozon.dto.account.AccountCreateDto;
import com.hse.gozon.dto.account.AccountDto;
import com.hse.gozon.dto.account.DepDto;
import com.hse.gozon.paymentsservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public AccountDto createAccount(@RequestBody AccountCreateDto newAccount){
        return accountService.createAccount(newAccount);
    }

    @GetMapping("/{accountId}")
    public AccountDto findById(@PathVariable Integer accountId){
        return accountService.findAccountById(accountId);
    }

    @PatchMapping("/deposit")
    public AccountDto deposit(@RequestBody DepDto depositDto){
        return accountService.deposit(depositDto);
    }
}
