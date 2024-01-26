package com.hg.bethunger.model;

import com.hg.bethunger.model.compositekeys.GameItemKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "games_items")
public class GameItem {
    @EmbeddedId
    private GameItemKey id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private boolean isAvailable = true;

    public GameItem(Game game, Item item) {
        this.id = new GameItemKey(game.getId(), item.getId());
        this.game = game;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameItem gameItem = (GameItem) o;
        return id.equals(gameItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}