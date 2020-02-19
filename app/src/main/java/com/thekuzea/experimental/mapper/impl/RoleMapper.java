package com.thekuzea.experimental.mapper.impl;

import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements Mapper<RoleResource, Role> {

    @Override
    public Role mapToModel(final RoleResource resource) {
        return Role.builder()
                .name(resource.getName())
                .build();
    }

    @Override
    public RoleResource mapToDto(final Role entity) {
        return RoleResource.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .build();
    }
}
