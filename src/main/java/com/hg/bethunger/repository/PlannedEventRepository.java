package com.hg.bethunger.repository;

import com.hg.bethunger.model.PlannedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannedEventRepository extends JpaRepository<PlannedEvent, Long> {
}