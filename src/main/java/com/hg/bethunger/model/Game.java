package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.GameStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private GameStatus status = GameStatus.DRAFT;

    private LocalDateTime dateStart;

    private Duration duration;

    private String description;

    private String arenaType;

    private String arenaDescription;

    @OneToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private Player winner;

    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @OneToMany(mappedBy = "game")
    private List<GameItem> items;

    @OneToMany(mappedBy = "game")
    private List<EventType> eventTypes;

    @OneToMany(mappedBy = "game")
    @OrderBy("startAt")
    private List<PlannedEvent> plannedEvents;

    @OneToMany(mappedBy = "game")
    @OrderBy("happenedAt DESC")
    private List<HappenedEvent> happenedEvents;

    public void addPlayer(Player player) {
        if (player != null) {
            if (this.players == null) {
                this.players = new ArrayList<>();
            }
            this.players.add(player);
            player.setGame(this);
        }
    }

    public void removePlayer(Player player) {
        if (player != null) {
            this.players.remove(player);
            player.setGame(null);
        }
    }

    public void addEventType(EventType eventType) {
        if (eventType != null) {
            if (this.eventTypes == null) {
                this.eventTypes = new ArrayList<>();
            }
            this.eventTypes.add(eventType);
            eventType.setGame(this);
        }
    }

    public void updateStatus(GameStatus status) throws RuntimeException {
        switch (status) {
            case PLANNED -> {
                if (this.status != GameStatus.DRAFT) {
                    throw new RuntimeException("The specified game cannot be published");
                }
            }
            case ONGOING -> {
                if (this.status != GameStatus.PLANNED) {
                    throw new RuntimeException("The specified game cannot be started");
                }
            }
            case COMPLETED -> {
                if (this.status != GameStatus.ONGOING) {
                    throw new RuntimeException("The specified game cannot be finished");
                }
            }
        }
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}