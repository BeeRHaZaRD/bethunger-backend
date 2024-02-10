package com.hg.bethunger.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GameUpdateDTO {
    @NotBlank
    @Size(max = 32)
    private String name;

    @NotNull
    @Future
    private LocalDateTime dateStart;

    @NotBlank
    private String description;

    @NotBlank
    private String arenaType;

    @NotBlank
    private String arenaDescription;
}
