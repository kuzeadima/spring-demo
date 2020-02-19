package com.thekuzea.experimental.util;

import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.constant.RoleMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RoleTestDataGenerator {

    public static Role createRole() {
        return Role.builder()
                .id(UUID.fromString("00000001-0001-0001-0001-000000000001"))
                .name("user")
                .build();
    }

    public static List<Role> createRoleList() {
        final Role role1 = Role.builder()
                .name("admin")
                .build();

        final Role role2 = Role.builder()
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }

    public static RoleResource createRoleResource() {
        return RoleResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .name("user")
                .build();
    }

    public static List<RoleResource> createRoleResourceList() {
        final RoleResource role1 = RoleResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .name("admin")
                .build();

        final RoleResource role2 = RoleResource.builder()
                .id("00000002-0002-0002-0002-000000000002")
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }

    public static RoleResource createRoleResourceWithErrorMessage() {
        final List<String> validationMessage = new ArrayList<>();
        validationMessage.add(RoleMessages.ROLE_ALREADY_EXISTS);

        return RoleResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .name("user")
                .validationMessages(validationMessage)
                .build();
    }
}
