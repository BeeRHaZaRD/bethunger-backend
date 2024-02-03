package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.EventTypeCreateDTO;
import com.hg.bethunger.dto.EventTypeDTO;
import com.hg.bethunger.model.EventType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventTypeMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public EventTypeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EventTypeDTO toDto(EventType eventType) {
        return modelMapper.map(eventType, EventTypeDTO.class);
    }

    public EventType toEntity(EventTypeCreateDTO eventTypeCreateDTO) {
        return modelMapper.map(eventTypeCreateDTO, EventType.class);
    }
}
