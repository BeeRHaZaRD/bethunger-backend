package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.AccountBalanceDTO;
import com.hg.bethunger.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountBalanceDTO toAccountBalanceDTO(Account account) {
        return modelMapper.map(account, AccountBalanceDTO.class);
    }
}
