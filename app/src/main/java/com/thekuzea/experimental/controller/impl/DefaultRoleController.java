package com.thekuzea.experimental.controller.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.controller.RoleController;
import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.service.RoleService;

@RestController
@RequiredArgsConstructor
public class DefaultRoleController implements RoleController {

    private final RoleService roleService;

    @Override
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Override
    public ResponseEntity<RoleDto> addNewRole(final RoleDto incomingResource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addNewRole(incomingResource));
    }

    @Override
    public ResponseEntity<Void> deleteByName(final String name) {
        roleService.deleteByName(name);
        return ResponseEntity.accepted().build();
    }
}
