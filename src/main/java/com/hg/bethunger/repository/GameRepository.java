package com.hg.bethunger.repository;

import com.hg.bethunger.model.Game;
import com.hg.bethunger.model.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);
    List<Game> findAllByStatus(GameStatus status);
    List<Game> findAllByStatusIn(Collection<GameStatus> statuses);
}