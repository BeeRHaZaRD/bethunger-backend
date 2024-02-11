package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.HappenedEventType;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "happened_events")
public class HappenedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private HappenedEventType type;

    @NotNull
    private LocalDateTime happenedAt = LocalDateTime.now();

    public HappenedEvent(HappenedEventType type, Game game, LocalDateTime happenedAt) {
        this.type = type;
        this.game = game;
        this.happenedAt = happenedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HappenedEvent)) return false;
        HappenedEvent that = (HappenedEvent) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}