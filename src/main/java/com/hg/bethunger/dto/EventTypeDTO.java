package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventTypeDTO {
    private Long id;
    private String name;
    private String description;
}
