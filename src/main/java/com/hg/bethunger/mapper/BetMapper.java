package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.BetCreateDTO;
import com.hg.bethunger.dto.BetDTO;
import com.hg.bethunger.model.Bet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public BetMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        modelMapper.createTypeMap(Bet.class, BetDTO.class)
            .addMappings(mapper -> mapper.map(src -> src.getPlayer().getGame(), BetDTO::setGame));
    }

    public Bet toEntity(BetCreateDTO betCreateDTO) {
        return modelMapper.map(betCreateDTO, Bet.class);
    }

    public BetDTO toDto(Bet bet) {
        return modelMapper.map(bet, BetDTO.class);
    }
}
