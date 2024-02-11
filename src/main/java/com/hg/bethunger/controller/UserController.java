package com.hg.bethunger.controller;

import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam(required = false) UserRole role) {
        if (role == null) {
            return userService.getUsers();
        } else {
            return userService.getUsersByRole(role);
        }
    }

    @GetMapping(path = "/{userId}")
    public UserDTO getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createManager(@RequestBody @Valid UserCreateDTO dto) {
        return userService.createUser(dto, UserRole.MANAGER);
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
