package com.thekuzea.experimental.controller.impl;

import com.thekuzea.experimental.controller.RoleController;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.exception.RoleNotFoundException;
import com.thekuzea.experimental.service.RoleService;
import com.thekuzea.experimental.constant.RoleMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    private final RoleService roleService;

    @Override
    public ResponseEntity<?> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addNewRole(final RoleResource incomingResource) {
        final RoleResource outgoingResource = roleService.addNewRole(incomingResource);

        if (outgoingResource.getValidationMessages().isEmpty()) {
            return new ResponseEntity<>(outgoingResource, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(outgoingResource, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deleteByName(final String name) {
        try {
            final RoleResource userResource = roleService.deleteByName(name);
            return new ResponseEntity<>(userResource, HttpStatus.ACCEPTED);
        } catch (RoleNotFoundException e) {
            return new ResponseEntity<>(RoleMessages.ROLE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
    }
}
