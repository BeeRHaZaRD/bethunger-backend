package com.hg.bethunger.repository;

import com.hg.bethunger.model.Bet;
import com.hg.bethunger.model.enums.BetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    @Modifying
    @Query("UPDATE Bet bets " +
            "SET bets.status = :status " +
            "WHERE bets.player.id = :playerId")
    void updateStatusByPlayerId(@Param("playerId") Long playerId, @Param("status") BetStatus status);

    List<Bet> findAllByPlayerId(Long playerId);
}