package com.hg.bethunger.controller;

import com.hg.bethunger.dto.AccountBalanceDTO;
import com.hg.bethunger.dto.AccountOperationDTO;
import com.hg.bethunger.security.UserPrincipal;
import com.hg.bethunger.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/balance")
    public AccountBalanceDTO getBalance(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return accountService.getBalance(userPrincipal.getUser());
    }

    @PostMapping(path = "/deposit")
    public AccountBalanceDTO deposit(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @RequestBody @Valid AccountOperationDTO accountOperationDTO) {
        return accountService.deposit(userPrincipal.getUser(), accountOperationDTO.getAmount());
    }

    @PostMapping(path = "/withdraw")
    public AccountBalanceDTO withdraw(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestBody @Valid AccountOperationDTO accountOperationDTO) {
        return accountService.withdraw(userPrincipal.getUser(), accountOperationDTO.getAmount());
    }
}
