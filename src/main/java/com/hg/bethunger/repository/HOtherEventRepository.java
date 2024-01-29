package com.hg.bethunger.repository;

import com.hg.bethunger.model.HOtherEvent;
import com.hg.bethunger.model.HappenedEvent;

import java.util.List;

public interface HOtherEventRepository extends HappenedEventRepository<HOtherEvent> {
    List<HappenedEvent> findAllByGameIdAndPlayerIdOrderByHappenedAtDesc(Long gameId, Long playerId);
}
