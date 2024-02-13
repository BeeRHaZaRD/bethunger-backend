package com.hg.bethunger.model.compositekeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GameItemKey implements Serializable {
    @NotNull
    @Column(name = "game_id")
    private Long gameId;

    @NotNull
    @Column(name = "item_id")
    private Long itemId;

    public GameItemKey(Long gameId, Long itemId) {
        this.gameId = gameId;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameItemKey that = (GameItemKey) o;
        return gameId.equals(that.gameId) && itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, itemId);
    }
}