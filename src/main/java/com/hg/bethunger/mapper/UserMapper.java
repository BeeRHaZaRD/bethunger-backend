package com.hg.bethunger.mapper;

import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public User toEntity(UserCreateDTO userCreateDTO) {
        return modelMapper.map(userCreateDTO, User.class);
    }
}
