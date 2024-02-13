package com.hg.bethunger.model;

import com.hg.bethunger.model.compositekeys.GameItemKey;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    private static final int playersNum = 24;

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

    public boolean isDraft() {
        return this.status == GameStatus.DRAFT;
    }

    public boolean isPlanned() {
        return this.status == GameStatus.PLANNED;
    }

    public boolean isOngoing() {
        return this.status == GameStatus.ONGOING;
    }

    public boolean isCompleted() {
        return this.status == GameStatus.COMPLETED;
    }

    public Optional<GameItem> getGameItem(Long itemId) {
        return this.items.stream().filter(gameItem ->
            gameItem.getId().equals(new GameItemKey(this.getId(), itemId))
        ).findFirst();
    }

    public boolean isInfoValid() {
        return this.name != null && !this.name.isBlank()
            && this.description != null && !this.description.isBlank()
            && this.arenaType != null && !this.arenaType.isBlank()
            && this.arenaDescription != null && !this.arenaDescription.isBlank()
            && this.dateStart != null;
    }

    public boolean isPlayersFull() {
        return this.players != null
            && this.players.size() == playersNum;
    }

    public boolean isPlayersTrainResultsFull() {
        return this.players != null
            && this.players.stream().allMatch(player -> player.getTrainResults() != null);
    }

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

    public int playersLeft() {
        return (int) this.players.stream().filter(player -> player.getStatus() != PlayerStatus.DEAD).count();
    }

    public boolean isPlayerSlotFree(Integer district, Sex sex) {
        return this.players.stream().noneMatch(player ->
            player.getDistrict().equals(district) && player.getSex() == sex
        );
    }

    public void updateStatus(GameStatus status) throws IllegalStateException {
        switch (status) {
            case PLANNED -> {
                if (this.status != GameStatus.DRAFT) {
                    throw new IllegalStateException("Данная игра не может быть опубликована");
                }
            }
            case ONGOING -> {
                if (this.status != GameStatus.PLANNED) {
                    throw new IllegalStateException("Данная игра не может быть запущена");
                }
            }
            case COMPLETED -> {
                if (this.status != GameStatus.ONGOING) {
                    throw new IllegalStateException("Данная игра не может быть завершена");
                }
            }
        }
        this.status = status;
    }

    /**
     * Caller must check if only last player is left
     */
    public void setWinnerOnComplete() throws RuntimeException {
        this.winner = this.players.stream()
            .filter(player -> player.getStatus() != PlayerStatus.DEAD)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid state: no players left"));
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