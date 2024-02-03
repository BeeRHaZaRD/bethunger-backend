package com.hg.bethunger.controller;

import com.hg.bethunger.dto.AuthDTO;
import com.hg.bethunger.dto.AuthResponseDTO;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/register")
    public AuthResponseDTO register(@RequestBody UserCreateDTO dto) {
        return authenticationService.register(dto);
    }

    @PostMapping(path = "/login")
    public AuthResponseDTO login(@RequestBody AuthDTO dto) {
        return authenticationService.login(dto);
    }
}
