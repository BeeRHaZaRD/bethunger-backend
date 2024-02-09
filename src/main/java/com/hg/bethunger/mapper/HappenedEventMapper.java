package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.*;
import com.hg.bethunger.dto.controlsystem.HappenedEventCreateDTO;
import com.hg.bethunger.exception.MissingParameterException;
import com.hg.bethunger.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HappenedEventMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public HappenedEventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        modelMapper.typeMap(HPlayerEvent.class, HappenedEventDTO.class)
            .setProvider(request -> modelMapper.map(request.getSource(), HPlayerEventDTO.class));
        modelMapper.typeMap(HPlannedEvent.class, HappenedEventDTO.class)
            .setProvider(request -> modelMapper.map(request.getSource(), HPlannedEventDTO.class));
        modelMapper.typeMap(HSupplyEvent.class, HappenedEventDTO.class)
            .setProvider(request -> modelMapper.map(request.getSource(), HSupplyEventDTO.class));
        modelMapper.typeMap(HOtherEvent.class, HappenedEventDTO.class)
            .setProvider(request -> modelMapper.map(request.getSource(), HOtherEventDTO.class));
    }

    public HappenedEventDTO toDTO(HappenedEvent happenedEvent) {
        return modelMapper.map(happenedEvent, HappenedEventDTO.class);
    }

    public HappenedEvent toEntity(HappenedEventCreateDTO happenedEventCreateDTO) {
        Class<? extends HappenedEvent> targetClass = null;
        switch (happenedEventCreateDTO.getType()) {
            case PLAYER -> {
                if (happenedEventCreateDTO.getPlayerId() == null) {
                    throw new MissingParameterException("playerId");
                }
                if (happenedEventCreateDTO.getPlayerEventType() == null) {
                    throw new MissingParameterException("playerEventType");
                }
                targetClass = HPlayerEvent.class;
            }
            case PLANNED_EVENT -> {
                if (happenedEventCreateDTO.getPlannedEventId() == null) {
                    throw new MissingParameterException("plannedEventId");
                }
                targetClass = HPlannedEvent.class;
            }
            case SUPPLY -> {
                if (happenedEventCreateDTO.getSupplyId() == null) {
                    throw new MissingParameterException("supplyId");
                }
                targetClass = HSupplyEvent.class;
            }
            case OTHER -> {
                if (happenedEventCreateDTO.getMessage() == null) {
                    throw new MissingParameterException("message");
                }
                targetClass = HOtherEvent.class;
            }
        }
        return modelMapper.map(happenedEventCreateDTO, targetClass);
    }
}
