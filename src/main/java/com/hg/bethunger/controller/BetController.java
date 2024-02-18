package com.hg.bethunger.controller;

import com.hg.bethunger.dto.BetCreateDTO;
import com.hg.bethunger.dto.BetDTO;
import com.hg.bethunger.security.UserPrincipal;
import com.hg.bethunger.service.BetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bets")
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @GetMapping
    public List<BetDTO> getBetsByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return betService.getBetsByUser(userPrincipal.getUser());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BetDTO createBet(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid BetCreateDTO dto) {
        return betService.createBet(userPrincipal.getUser(), dto);
    }
}
