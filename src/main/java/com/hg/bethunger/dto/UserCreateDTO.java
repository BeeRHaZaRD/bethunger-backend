package com.hg.bethunger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateDTO {
    @NotNull
    @Size(min = 3, max = 32)
    private String username;

    @NotNull
    @Size(min = 3, max = 32)
    private String password;

    @NotNull
    @Size(min = 2, max = 16)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 16)
    private String lastName;

    public UserCreateDTO(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
