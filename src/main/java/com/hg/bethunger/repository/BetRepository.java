package com.hg.bethunger.repository;

import com.hg.bethunger.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
}