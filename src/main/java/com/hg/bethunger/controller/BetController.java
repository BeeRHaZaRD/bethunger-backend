package com.hg.bethunger.controller;

import com.hg.bethunger.dto.BetCreateDTO;
import com.hg.bethunger.dto.BetDTO;
import com.hg.bethunger.mapper.BetMapper;
import com.hg.bethunger.model.Bet;
import com.hg.bethunger.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bets")
public class BetController {
    private final BetService betService;
    private final BetMapper betMapper;

    @Autowired
    public BetController(BetService betService, BetMapper betMapper) {
        this.betService = betService;
        this.betMapper = betMapper;
    }

    @PostMapping
    public BetDTO createBet(BetCreateDTO betCreateDTO) {
        Bet bet = betMapper.toEntity(betCreateDTO);
        return null;
    }
}
