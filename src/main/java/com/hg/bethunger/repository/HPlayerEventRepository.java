package com.hg.bethunger.repository;

import com.hg.bethunger.model.HPlayerEvent;
import com.hg.bethunger.model.HappenedEvent;

import java.util.List;

public interface HPlayerEventRepository extends HappenedEventRepository<HPlayerEvent> {
    List<HappenedEvent> findAllByGameIdAndPlayerIdOrderByHappenedAtDesc(Long gameId, Long playerId);
}
