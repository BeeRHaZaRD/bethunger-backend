package com.hg.bethunger.dto;

import com.hg.bethunger.model.Supply;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HSupplyEventDTO extends HappenedEventDTO {
    // TODO SupplyDTO
    private Supply supply;
}
