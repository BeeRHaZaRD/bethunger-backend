package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.ItemDTO;
import com.hg.bethunger.model.Item;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Item toEntity(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    public ItemDTO toDto(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }
}
