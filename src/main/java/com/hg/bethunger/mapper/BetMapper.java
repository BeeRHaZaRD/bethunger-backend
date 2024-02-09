package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.BetCreateDTO;
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
    }

    public Bet toEntity(BetCreateDTO betCreateDTO) {
        return modelMapper.map(betCreateDTO, Bet.class);
    }
}
