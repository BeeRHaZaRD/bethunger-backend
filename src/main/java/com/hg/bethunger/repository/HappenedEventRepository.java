package com.hg.bethunger.repository;

import com.hg.bethunger.model.HappenedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HappenedEventRepository extends JpaRepository<HappenedEvent, Long> {
    List<HappenedEvent> findAllByGameIdAndHappenedAtAfterOrderByHappenedAtDesc(Long gameId, LocalDateTime after);
}