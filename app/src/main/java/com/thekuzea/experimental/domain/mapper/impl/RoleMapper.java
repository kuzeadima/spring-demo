package com.thekuzea.experimental.domain.mapper.impl;

import org.springframework.stereotype.Component;

import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.mapper.Mapper;
import com.thekuzea.experimental.domain.model.Role;

@Component
public class RoleMapper implements Mapper<RoleDto, Role> {

    @Override
    public Role mapToModel(final RoleDto resource) {
        return Role.builder()
                .name(resource.getName())
                .build();
    }

    @Override
    public RoleDto mapToDto(final Role entity) {
        return RoleDto.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .build();
    }
}
