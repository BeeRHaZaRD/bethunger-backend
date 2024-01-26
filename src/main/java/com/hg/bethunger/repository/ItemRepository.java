package com.hg.bethunger.repository;

import com.hg.bethunger.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}