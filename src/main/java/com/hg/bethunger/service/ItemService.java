package com.hg.bethunger.service;

import com.hg.bethunger.dto.ItemDTO;
import com.hg.bethunger.mapper.ItemMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public List<ItemDTO> getItems() {
        return MappingUtils.mapList(
            itemRepository.findAll(), itemMapper::toDto
        );
    }
}
