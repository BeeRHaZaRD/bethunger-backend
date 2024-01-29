package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.PlannedEventStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "planned_events")
public class PlannedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventType eventType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    private LocalDateTime startAt = LocalDateTime.now();

    @Enumerated(EnumType.ORDINAL)
    private PlannedEventStatus status = PlannedEventStatus.SCHEDULED;

    @OneToOne(mappedBy = "plannedEvent")
    private HPlannedEvent happenedPlannedEvent;

    public PlannedEvent(PlannedEventStatus status, EventType eventType, Game game, LocalDateTime startAt) {
        this.status = status;
        this.eventType = eventType;
        this.game = game;
        this.startAt = startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlannedEvent that = (PlannedEvent) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}