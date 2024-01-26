package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GameItemDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
}
