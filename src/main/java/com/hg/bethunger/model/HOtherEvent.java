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
@Table(name = "h_other_events")
@PrimaryKeyJoinColumn(name = "happened_event_id")
public class HOtherEvent extends HappenedEvent {
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @NotNull
    private String message;

    public HOtherEvent(Game game, HappenedEventType type, LocalDateTime happenedAt, Player player, String message) {
        super(game, type, happenedAt);
        this.player = player;
        this.message = message;
    }

    public HOtherEvent(Game game, HappenedEventType type, LocalDateTime happenedAt, String message) {
        super(game, type, happenedAt);
        this.message = message;
    }
}
