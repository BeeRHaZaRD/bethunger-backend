package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.exception.ResourceAlreadyExistsException;
import com.hg.bethunger.exception.ResourceNotFoundException;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.UserMapper;
import com.hg.bethunger.model.User;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUserById(Long id) {
        User user = Utils.findByIdOrThrow(userRepository, id, "User");
        return userMapper.toUserDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new ResourceNotFoundException("User", "username", username));

        return userMapper.toUserDto(user);
    }

    public List<UserDTO> getUsers() {
        return MappingUtils.mapList(
            userRepository.findAll(), userMapper::toUserDto
        );
    }

    public List<UserDTO> getUsersByRole(UserRole role) {
        return MappingUtils.mapList(
            userRepository.findAllByRole(role), userMapper::toUserDto
        );
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO dto, UserRole userRole) {
        User user = userMapper.toEntity(dto);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResourceAlreadyExistsException("Пользователь с именем " + user.getUsername() + " уже существует");
        }
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserDto(
            userRepository.save(user)
        );
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
