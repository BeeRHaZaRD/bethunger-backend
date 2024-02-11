package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.dto.PlayerInfoDTO;
import com.hg.bethunger.dto.TrainResultsDTO;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlayerMapper;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.TrainResults;
import com.hg.bethunger.model.enums.Sex;
import com.hg.bethunger.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public PlayerDTO getPlayerById(Long id) {
        Player player = Utils.findByIdOrThrow(playerRepository, id, "Player");
        return playerMapper.toDto(player);
    }

    public List<PlayerInfoDTO> getPlayers() {
        return MappingUtils.mapList(
            playerRepository.findAll(), playerMapper::toInfoDTO
        );
    }

    public List<PlayerInfoDTO> getAvailablePlayers(Integer district, Sex sex) {
        return MappingUtils.mapList(
            playerRepository.findAllByDistrictAndSexAndGameIsNull(district, sex), playerMapper::toInfoDTO
        );
    }

    @Transactional
    public void updateTrainResults(Long playerId, TrainResultsDTO trainResultsDTO) {
        Player player = Utils.findByIdOrThrow(playerRepository, playerId, "Player");

        TrainResults trainResults = playerMapper.trainResultsDTOtoEntity(trainResultsDTO);
        player.setTrainResults(trainResults);
    }
}
