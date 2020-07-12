package com.thekuzea.experimental.domain.mapper.impl;

import org.springframework.stereotype.Component;

import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.mapper.Mapper;
import com.thekuzea.experimental.domain.model.User;

@Component
public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public User mapToModel(final UserDto resource) {
        return User.builder()
                .username(resource.getUsername())
                .password(resource.getPassword())
                .build();
    }

    @Override
    public UserDto mapToDto(final User entity) {
        return UserDto.builder()
                .id(entity.getId().toString())
                .username(entity.getUsername())
                .role(entity.getRole().getName())
                .build();
    }
}
