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
@Table(name = "h_supply_events")
@PrimaryKeyJoinColumn(name = "happened_event_id")
public class HSupplyEvent extends HappenedEvent {
    @NotNull
    @OneToOne
    @JoinColumn(name = "supply_id", referencedColumnName = "id")
    private Supply supply;

    public HSupplyEvent(Game game, HappenedEventType type, LocalDateTime happenedAt, Supply supply) {
        super(game, type, happenedAt);
        this.supply = supply;
    }
}