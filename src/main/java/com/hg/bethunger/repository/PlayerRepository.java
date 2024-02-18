package com.hg.bethunger.repository;

import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByDistrictAndSexAndGameIsNull(Integer district, Sex sex);

    @Query(value = "SELECT * " +
        "FROM players " +
        "WHERE game_id = :gameId AND odd IS NOT NULL",
        nativeQuery = true)
    List<Player> getPlayerOddsByGameId(@Param("gameId") Long gameId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE players " +
        "SET odd = (SELECT SUM(b.amount) * (1.0 - :margin) " +
        "                   FROM bets b " +
        "                      JOIN players p ON b.player_id = p.id " +
        "                   WHERE p.game_id = :gameId" +
        "                   GROUP BY p.game_id) / (SELECT SUM(b.amount) " +
        "                                                 FROM bets b " +
        "                                                     JOIN players p ON b.player_id = p.id " +
        "                                                 WHERE p.id = players.id " +
        "                                                 GROUP BY p.id) " +
        "WHERE game_id = :gameId AND id IN (SELECT DISTINCT player_id FROM bets)",
        nativeQuery = true)
    void updateOddsByGameId(@Param("gameId") Long gameId, @Param("margin") Double margin);
}
