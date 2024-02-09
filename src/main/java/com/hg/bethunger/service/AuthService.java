package com.hg.bethunger.service;

import com.hg.bethunger.dto.AuthDTO;
import com.hg.bethunger.dto.AuthResponseDTO;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.mapper.UserMapper;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.security.JWTUtil;
import com.hg.bethunger.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(UserService userService, JWTUtil jwtUtil, AuthenticationManager authManager, UserMapper userMapper) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userMapper = userMapper;
    }

    public AuthResponseDTO register(UserCreateDTO dto) {
        UserDTO userDTO = userService.createUser(dto, UserRole.USER);
        String token = jwtUtil.generateToken(dto.getUsername());
        return new AuthResponseDTO(token, userDTO);
    }

    public AuthResponseDTO login(AuthDTO authDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
        Authentication authentication = authManager.authenticate(authInputToken);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO userDTO = userMapper.toDto(userPrincipal.getUser());
        String token = jwtUtil.generateToken(authDto.getUsername());
        return new AuthResponseDTO(token, userDTO);
    }
}
