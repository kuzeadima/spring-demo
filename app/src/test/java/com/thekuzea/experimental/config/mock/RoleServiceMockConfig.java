package com.thekuzea.experimental.config.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.thekuzea.experimental.service.RoleService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_ALREADY_EXISTS;
import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceAsNewRole;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceAsNewRoleResponse;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceList;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceNegativeFlow;

@TestConfiguration
public class RoleServiceMockConfig {

    @Bean
    @Primary
    public RoleService roleService() {
        final RoleService roleService = mock(RoleService.class);

        when(roleService.getAllRoles()).thenReturn(createRoleResourceList());
        doThrow(new IllegalArgumentException(ROLE_NOT_FOUND)).when(roleService).deleteByName("moderator");

        when(roleService.addNewRole(refEq(createRoleResourceAsNewRole())))
                .thenReturn(createRoleResourceAsNewRoleResponse());
        when(roleService.addNewRole(refEq(createRoleResourceNegativeFlow())))
                .thenThrow(new IllegalArgumentException(ROLE_ALREADY_EXISTS));

        return roleService;
    }
}
