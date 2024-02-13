package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.SupplyCreateDTO;
import com.hg.bethunger.dto.SupplyDTO;
import com.hg.bethunger.model.Supply;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupplyMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public SupplyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Supply toEntity(SupplyCreateDTO supplyCreateDTO) {
        return modelMapper.map(supplyCreateDTO, Supply.class);
    }

    public SupplyDTO toDto(Supply supply) {
        return modelMapper.map(supply, SupplyDTO.class);
    }
}
