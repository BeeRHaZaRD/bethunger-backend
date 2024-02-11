package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.HPlayerEventType;
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
@Table(name = "h_player_events")
@PrimaryKeyJoinColumn(name = "happened_event_id")
public class HPlayerEvent extends HappenedEvent {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private HPlayerEventType playerEventType;

    public HPlayerEvent(Game game, LocalDateTime happenedAt, Player player, HPlayerEventType playerEventType) {
        super(HappenedEventType.PLAYER, game, happenedAt);
        this.player = player;
        this.playerEventType = playerEventType;
    }
}