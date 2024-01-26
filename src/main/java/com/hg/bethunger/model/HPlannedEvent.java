package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.HappenedEventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "h_planned_events")
@PrimaryKeyJoinColumn(name = "happened_event_id")
public class HPlannedEvent extends HappenedEvent {
    @NotNull
    @OneToOne
    @JoinColumn(name = "planned_event_id", referencedColumnName = "id")
    private PlannedEvent plannedEvent;

    public HPlannedEvent(Game game, HappenedEventType type, LocalDateTime happenedAt, PlannedEvent plannedEvent) {
        super(game, type, happenedAt);
        this.plannedEvent = plannedEvent;
    }
}