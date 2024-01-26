package com.hg.bethunger.repository;

import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.Sex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByDistrictAndSexAndGameIsNull(Integer district, Sex sex);
}