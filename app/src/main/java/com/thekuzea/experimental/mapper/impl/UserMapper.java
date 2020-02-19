package com.thekuzea.experimental.mapper.impl;

import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper implements Mapper<UserResource, User> {

    @Override
    public User mapToModel(final UserResource resource) {
        return User.builder()
                .username(resource.getUsername())
                .password(resource.getPassword())
                .build();
    }

    @Override
    public UserResource mapToDto(final User entity) {
        return UserResource.builder()
                .id(entity.getId().toString())
                .username(entity.getUsername())
                .role(entity.getRole().getName())
                .validationMessages(new ArrayList<>())
                .build();
    }
}
