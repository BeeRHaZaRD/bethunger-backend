package com.hg.bethunger.controller;

import com.hg.bethunger.dto.AuthDTO;
import com.hg.bethunger.dto.AuthResponseDTO;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@RequestBody @Valid UserCreateDTO dto) {
        return authService.register(dto);
    }

    @PostMapping(path = "/login")
    public AuthResponseDTO login(@RequestBody @Valid AuthDTO dto) {
        return authService.login(dto);
    }
}
