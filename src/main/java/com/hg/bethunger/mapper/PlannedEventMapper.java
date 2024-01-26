package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.PlannedEventCreateDTO;
import com.hg.bethunger.dto.PlannedEventDTO;
import com.hg.bethunger.model.PlannedEvent;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlannedEventMapper {
    private final ModelMapper modelMapper;

    public PlannedEventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PlannedEventDTO toDto(PlannedEvent plannedEvent) {
        return modelMapper.map(plannedEvent, PlannedEventDTO.class);
    }

    public PlannedEvent toEntity(PlannedEventCreateDTO plannedEventCreateDTO) {
        return modelMapper.map(plannedEventCreateDTO, PlannedEvent.class);
    }
}
