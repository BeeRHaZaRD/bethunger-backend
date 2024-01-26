package com.hg.bethunger.service;

import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlayerMapper;
import com.hg.bethunger.model.enums.Sex;
import com.hg.bethunger.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerDTO> getAvailablePlayers(Integer district, Sex sex) {
        return MappingUtils.mapList(
            playerRepository.findAllByDistrictAndSexAndGameIsNull(district, sex), playerMapper::toDto
        );
    }
}
