package com.hg.bethunger.service;

import com.hg.bethunger.model.Bet;
import com.hg.bethunger.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BetService {
    private final BetRepository betRepository;

    @Autowired
    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @Transactional
    public Bet createBet(Bet bet) {
        return null;
    }
}
