package com.hg.bethunger.repository;

import com.hg.bethunger.model.User;
import com.hg.bethunger.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findAllByRole(UserRole role);

    @Modifying
    @Query(value = "UPDATE users " +
        "SET balance = balance + (SELECT SUM(amount * odd) " +
        "                         FROM bets b " +
        "                              JOIN players p ON p.id = b.player_id " +
        "                         WHERE b.user_id = users.id AND b.player_id = :playerId " +
        "                         GROUP BY b.user_id) " +
        "WHERE users.id IN (SELECT u.id " +
        "                   FROM users u " +
        "                      JOIN bets b ON b.user_id = u.id " +
        "                   WHERE b.player_id = :playerId)",
        nativeQuery = true)
    void addWinningsToUsers(@Param("playerId") Long playerId);

    @Modifying
    @Query(value = "UPDATE users " +
        "SET balance = balance + (SELECT SUM(amount) * (1.0 - :margin) " +
        "                         FROM bets b" +
        "                              JOIN players p ON p.id = b.player_id " +
        "                         WHERE p.game_id = :gameId AND b.user_id = users.id " +
        "                         GROUP BY b.user_id)" +
        "WHERE users.id IN (SELECT users.id " +
        "                   FROM users u " +
        "                      JOIN bets b ON b.user_id = u.id " +
        "                      JOIN players p ON b.player_id = p.id " +
        "                   WHERE p.game_id = :gameId)",
        nativeQuery = true)
    void returnFundsToUsers(@Param("gameId") Long gameId, @Param("margin") Double margin);

    @Modifying
    @Query(value = "UPDATE users " +
        "SET balance = balance - (SELECT COALESCE(SUM(bets.amount) * (1.0 - :margin), 0) " +
        "                         FROM bets " +
        "                               JOIN players ON bets.player_id = players.id" +
        "                         WHERE players.game_id = :gameId)" +
        "WHERE username = 'admin'",
        nativeQuery = true)
    void subtractMoneyFromAdmin(@Param("gameId") Long gameId, @Param("margin") Double margin);
}