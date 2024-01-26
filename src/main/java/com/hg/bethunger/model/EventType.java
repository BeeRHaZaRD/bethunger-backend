package com.hg.bethunger.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "event_types")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    private String name;

    private String description;

    @OneToMany(mappedBy = "eventType")
    private List<PlannedEvent> plannedEvents;

    public EventType(Game game, String name, String description) {
        this.game = game;
        this.name = name;
        this.description = description;
    }
}