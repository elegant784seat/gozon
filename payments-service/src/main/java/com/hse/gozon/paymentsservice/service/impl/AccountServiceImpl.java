package com.hse.gozon.paymentsservice.service.impl;

import com.hse.gozon.dto.account.AccountCreateDto;
import com.hse.gozon.dto.account.AccountDto;
import com.hse.gozon.dto.account.DepDto;
import com.hse.gozon.paymentsservice.exception.NotFoundException;
import com.hse.gozon.paymentsservice.model.Account;
import com.hse.gozon.paymentsservice.repository.AccountRepository;
import com.hse.gozon.paymentsservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hse.gozon.paymentsservice.mapper.AccountMapper.toDto;
import static com.hse.gozon.paymentsservice.mapper.AccountMapper.toEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountCreateDto newAccount) {
        Account account = accountRepository.save(toEntity(newAccount));
        log.debug("пользователь{} сохранен", account.getEmail());
        return toDto(account);
    }

    @Override
    public AccountDto findAccountById(Integer id) {
        Account account = getAccountById(id);
        log.debug("пользователь с id{} успешно найден", id);
        return toDto(account);

    }

    @Override
    @Transactional
    public AccountDto deposit(DepDto depDto) {
        Account account = getAccountById(depDto.getAccountId());
        accountRepository.deposit(depDto.getAccountId(), depDto.getAmount());
        log.debug("счет успешно пополнен{}", account.getBalance());
        return toDto(account);
    }

    private Account getAccountById(Integer id){
        return accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("пользователь с id " + " не найден"));
    }
}
