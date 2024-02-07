package com.hg.bethunger.service;

import com.hg.bethunger.dto.AccountBalanceDTO;
import com.hg.bethunger.mapper.AccountMapper;
import com.hg.bethunger.model.User;
import com.hg.bethunger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(UserRepository userRepository, AccountMapper accountMapper) {
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    public AccountBalanceDTO getBalance(User user) {
        return accountMapper.toAccountBalanceDTO(user.getAccount());
    }

    @Transactional
    public AccountBalanceDTO deposit(User user, BigDecimal amount) {
        user.getAccount().addMoney(amount);
        user = userRepository.save(user);

        return accountMapper.toAccountBalanceDTO(user.getAccount());
    }

    @Transactional
    public AccountBalanceDTO withdraw(User user, BigDecimal amount) {
        user.getAccount().subtractMoney(amount);
        user = userRepository.save(user);

        return accountMapper.toAccountBalanceDTO(user.getAccount());
    }
}
