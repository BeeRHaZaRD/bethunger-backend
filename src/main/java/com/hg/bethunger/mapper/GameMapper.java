package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.GameCreateDTO;
import com.hg.bethunger.dto.GameFullDTO;
import com.hg.bethunger.dto.GameInfoDTO;
import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.model.Game;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.Sex;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class GameMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public GameMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<Game, GameInfoDTO> gameToGameInfoDtoTypeMap = modelMapper.createTypeMap(Game.class, GameInfoDTO.class);
        TypeMap<Game, GameFullDTO> gameToGameFullDtoTypeMap = modelMapper.createTypeMap(Game.class, GameFullDTO.class);

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

        gameToGameInfoDtoTypeMap.addMappings(
            mapper -> mapper.using(durationConverter).map(Game::getDuration, GameInfoDTO::setDuration)
        );
        gameToGameFullDtoTypeMap.addMappings(mapper -> {
            mapper.using(durationConverter).map(Game::getDuration, GameFullDTO::setDuration);
            mapper.using(playersConverter).map(Game::getPlayers, GameFullDTO::setPlayers);
            mapper.when(Conditions.not(MappingUtils.isSuperUser)).skip(Game::getEventTypes, GameFullDTO::setEventTypes);
            mapper.when(Conditions.not(MappingUtils.isSuperUser)).skip(Game::getPlannedEvents, GameFullDTO::setPlannedEvents);
        });
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
}
