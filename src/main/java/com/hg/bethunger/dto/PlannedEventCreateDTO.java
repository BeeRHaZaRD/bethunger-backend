package com.hg.bethunger.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlannedEventCreateDTO {
    @NotNull
    @Positive
    private Long eventTypeId;

    @Future
    private LocalDateTime startAt;
}
