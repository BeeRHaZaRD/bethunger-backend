package com.hg.bethunger.repository;

import com.hg.bethunger.model.HappenedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HappenedEventRepository<T extends HappenedEvent> extends JpaRepository<T, Long> {
    List<HappenedEvent> findAllByGameIdOrderByHappenedAtDesc(Long gameId);
    List<HappenedEvent> findAllByGameIdAndHappenedAtAfterOrderByHappenedAtDesc(Long gameId, LocalDateTime after);
}