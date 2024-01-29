package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.exception.ResourceNotFoundException;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.UserMapper;
import com.hg.bethunger.model.User;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUserById(Long id) {
        User user = Utils.findByIdOrThrow(userRepository, id, "User");
        return userMapper.toDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new ResourceNotFoundException("User", "username", username));

        return userMapper.toDto(user);
    }

    public List<UserDTO> getUsers() {
        return MappingUtils.mapList(
            userRepository.findAll(), userMapper::toDto
        );
    }

    public List<UserDTO> getUsersByRole(UserRole role) {
        return MappingUtils.mapList(
            userRepository.findAllByRole(role), userMapper::toDto
        );
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO, UserRole userRole) {
        User user = userMapper.toEntity(userCreateDTO);
        user.setRole(userRole);

        return userMapper.toDto(
            userRepository.save(user)
        );
    }

    @Transactional
    public UserDTO updateUser(Long id, UserCreateDTO userCreateDTO) {
        User user = Utils.findByIdOrThrow(userRepository, id, "User");
        BeanUtils.copyProperties(userCreateDTO, user);

        return userMapper.toDto(
            userRepository.save(user)
        );
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
