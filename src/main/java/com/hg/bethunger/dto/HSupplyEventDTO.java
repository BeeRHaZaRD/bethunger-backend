package com.hg.bethunger.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HSupplyEventDTO extends HappenedEventDTO {
    private SupplyDTO supply;
}
