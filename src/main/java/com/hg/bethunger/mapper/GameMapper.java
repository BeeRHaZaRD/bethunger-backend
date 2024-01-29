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

import java.time.Duration;
import java.util.*;

@Component
public class GameMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public GameMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<Game, GameInfoDTO> gameToGameInfoDTOTypeMap = modelMapper.createTypeMap(Game.class, GameInfoDTO.class);
        TypeMap<Game, GameFullDTO> gameToGameFullDTOTypeMap = modelMapper.createTypeMap(Game.class, GameFullDTO.class);

        Converter<List<Player>, Map<Integer, List<PlayerDTO>>> playersConverter = ctx -> {
            List<Player> src = ctx.getSource();
            Map<Integer, List<PlayerDTO>> dest = GameFullDTO.initPlayers();
            if (src == null || src.isEmpty()) {
                return dest;
            }
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
        Converter<Duration, Long> durationConverter = ctx -> {
            Duration duration = ctx.getSource();
            return duration != null ? duration.getSeconds() : null;
        };

        gameToGameInfoDTOTypeMap.addMappings(
            mapper -> mapper.using(durationConverter).map(Game::getDuration, GameInfoDTO::setDuration)
        );
        gameToGameFullDTOTypeMap.addMappings(
            mapper -> mapper.using(durationConverter).map(Game::getDuration, GameFullDTO::setDuration)
        );
        gameToGameFullDTOTypeMap.addMappings(
            mapper -> mapper.using(playersConverter).map(Game::getPlayers, GameFullDTO::setPlayers)
        );
    }

    public GameFullDTO toFullDto(Game game) {
        GameFullDTO gameFullDTO = modelMapper.map(game, GameFullDTO.class);
        if (game.getWinner() != null) {
            gameFullDTO.getWinner().setFullName(game.getWinner().getFirstName() + ' ' + game.getWinner().getLastName());
        }
        return gameFullDTO;
    }

    public GameInfoDTO toInfoDto(Game game) {
        GameInfoDTO gameInfoDTO = modelMapper.map(game, GameInfoDTO.class);
        if (game.getWinner() != null) {
            gameInfoDTO.getWinner().setFullName(game.getWinner().getFirstName() + ' ' + game.getWinner().getLastName());
        }
        return gameInfoDTO;
    }

    public Game toEntity(GameCreateDTO gameCreateDTO) {
        return modelMapper.map(gameCreateDTO, Game.class);
    }
}
