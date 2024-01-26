package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.model.Player;
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
            mapper -> mapper.using(fullNameConverter).map(src -> src, PlayerDTO::setFullName)
        );
    }

    public PlayerDTO toDto(Player player) {
        return modelMapper.map(player, PlayerDTO.class);
    }
}
