package com.hg.bethunger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDTO {
    @NotNull
    @Size(min = 3, max = 32)
    private String username;

    @NotNull
    @Size(min = 3, max = 32)
    private String password;
}
