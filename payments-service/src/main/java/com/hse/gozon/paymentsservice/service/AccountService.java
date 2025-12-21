package com.hse.gozon.paymentsservice.service;

import com.hse.gozon.dto.account.AccountCreateDto;
import com.hse.gozon.dto.account.AccountDto;
import com.hse.gozon.dto.account.DepDto;

public interface AccountService {
    AccountDto createAccount(AccountCreateDto newAccount);

    AccountDto findAccountById(Integer id);

    AccountDto deposit(DepDto depDto);

}
