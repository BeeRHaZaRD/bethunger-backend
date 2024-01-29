package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.dto.PlayerInfoDTO;
import com.hg.bethunger.dto.TrainResultsDTO;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.TrainResults;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PlayerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        Converter<Player, String> fullNameConverter =
            ctx -> ctx.getSource().getFirstName() + " " + ctx.getSource().getLastName();
        modelMapper.createTypeMap(Player.class, PlayerDTO.class).addMappings(
            mapper -> mapper.using(fullNameConverter).map(player -> player, PlayerDTO::setFullName)
        );
        modelMapper.createTypeMap(Player.class, PlayerInfoDTO.class).addMappings(
            mapper -> mapper.using(fullNameConverter).map(player -> player, PlayerInfoDTO::setFullName)
        );
    }

    public PlayerDTO toDto(Player player) {
        return modelMapper.map(player, PlayerDTO.class);
    }

    public PlayerInfoDTO toInfoDTO(Player player) {
        return modelMapper.map(player, PlayerInfoDTO.class);
    }

    public TrainResults trainResultsDTOtoEntity(TrainResultsDTO trainResultsDTO) {
        return modelMapper.map(trainResultsDTO, TrainResults.class);
    }
}
