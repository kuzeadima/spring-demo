package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.domain.dto.RoleDto;

public interface RoleService {

    List<RoleDto> getAllRoles();

    RoleDto addNewRole(RoleDto incomingResource);

    void deleteByName(String name);
}
