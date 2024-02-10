package com.hg.bethunger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCreateDTO {
    @NotBlank
    @Size(max = 32)
    private String name;

    @NotNull
    @Positive
    private Long managerId;
}
