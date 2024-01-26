package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.GameItemDTO;
import com.hg.bethunger.model.GameItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameItemMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public GameItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(GameItem.class, GameItemDTO.class)
            .addMappings(mapper -> mapper.map(src -> src.getItem().getId(), GameItemDTO::setId))
            .addMappings(mapper -> mapper.map(src -> src.getItem().getName(), GameItemDTO::setName))
            .addMappings(mapper -> mapper.map(src -> src.getItem().getDescription(), GameItemDTO::setDescription))
            .addMappings(mapper -> mapper.map(src -> src.getItem().getPrice(), GameItemDTO::setPrice));
    }

    public GameItemDTO toDto(GameItem gameItem) {
        return modelMapper.map(gameItem, GameItemDTO.class);
    }
}
