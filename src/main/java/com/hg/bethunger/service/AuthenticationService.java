package com.hg.bethunger.service;

import com.hg.bethunger.dto.AuthDTO;
import com.hg.bethunger.dto.AuthResponseDTO;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthenticationService(UserService userService, JWTUtil jwtUtil, AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    public AuthResponseDTO register(UserCreateDTO dto) {
        UserDTO userDTO = userService.createUser(dto, UserRole.USER);
        String token = jwtUtil.generateToken(dto.getUsername());
        return new AuthResponseDTO(token, userDTO);
    }

    public AuthResponseDTO login(AuthDTO authDto) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

        try {
            authManager.authenticate(authInputToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Неверные логин/пароль");
        }

        String token = jwtUtil.generateToken(authDto.getUsername());
        UserDTO userDTO = userService.getUserByUsername(authDto.getUsername());
        return new AuthResponseDTO(token, userDTO);
    }
}
