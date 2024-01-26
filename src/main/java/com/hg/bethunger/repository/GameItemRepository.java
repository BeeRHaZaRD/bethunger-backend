package com.hg.bethunger.repository;

import com.hg.bethunger.model.GameItem;
import com.hg.bethunger.model.compositekeys.GameItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameItemRepository extends JpaRepository<GameItem, GameItemKey> {

}
