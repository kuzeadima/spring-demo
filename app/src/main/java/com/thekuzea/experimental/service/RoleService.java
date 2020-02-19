package com.thekuzea.experimental.service;

import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.exception.RoleNotFoundException;

import java.util.List;

public interface RoleService {

    List<RoleResource> getAllRoles();

    RoleResource addNewRole(RoleResource incomingResource);

    RoleResource deleteByName(String name) throws RoleNotFoundException;
}
