package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "players")
public class Player implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Integer district;

    private LocalDate birthDate;

    private Double odd;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PlayerStatus status = PlayerStatus.ALIVE;

    @Embedded
    private TrainResults trainResults;

    private LocalDateTime cooldownTo;

    @OneToOne(mappedBy = "winner")
    private Game winnerOf;

    @OneToMany(mappedBy = "player")
    private List<Bet> bets;

    @OneToMany(mappedBy = "player")
    private List<Supply> supplies;

    @OneToMany(mappedBy = "player")
    private List<HPlayerEvent> happenedPlayerEvents;

    @OneToMany(mappedBy = "player")
    private List<HOtherEvent> happenedOtherEvents;

    public Player(PlayerStatus status, String firstName, String lastName, Integer district, Sex sex) {
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.district = district;
        this.sex = sex;
    }

    public void updateStatus(PlayerStatus status) {
        if (status.ordinal() > this.status.ordinal()) {
            this.status = status;
        }
    }

    public boolean isSuppliable() {
        return this.cooldownTo == null || this.cooldownTo.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // TODO for initialiseDb
    @Override
    public Player clone() {
        Player clone = new Player();
        clone.status = this.status;
        clone.firstName = this.firstName;
        clone.lastName = this.lastName;
        clone.district = this.district;
        clone.sex = this.sex;
        return clone;
    }
}