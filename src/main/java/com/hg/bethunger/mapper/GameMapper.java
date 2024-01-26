package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.*;
import com.hg.bethunger.model.Game;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.Sex;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public GameMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<Game, GameFullDTO> typeMap = modelMapper.createTypeMap(Game.class, GameFullDTO.class);
        Converter<List<Player>, Map<Integer, List<PlayerDTO>>> playersConverter = ctx -> {
            List<Player> src = ctx.getSource();
            Map<Integer, List<PlayerDTO>> dest = GameFullDTO.initPlayers();
            src.forEach(player -> {
                PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
                Integer district = player.getDistrict();
                if (player.getSex() == Sex.MALE) {
                    dest.get(district).set(0, playerDTO);
                } else {
                    dest.get(district).set(1, playerDTO);
                }
            });
            return dest;
        };
        typeMap.addMappings(
            mapper -> mapper.using(playersConverter).map(Game::getPlayers, GameFullDTO::setPlayers)
        );
    }

    public GameFullDTO toFullDto(Game game) {
        return modelMapper.map(game, GameFullDTO.class);
    }

    public GameInfoDTO toInfoDto(Game game) {
        return modelMapper.map(game, GameInfoDTO.class);
    }

    public Game toEntity(GameCreateDTO gameCreateDTO) {
        return modelMapper.map(gameCreateDTO, Game.class);
    }

    public Game toEntity(GameUpdateDTO gameUpdateDTO) {
        return modelMapper.map(gameUpdateDTO, Game.class);
    }
}
