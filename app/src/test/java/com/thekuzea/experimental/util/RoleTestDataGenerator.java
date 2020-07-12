package com.thekuzea.experimental.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.model.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleTestDataGenerator {

    public static Role createUserRole() {
        return Role.builder()
                .id(UUID.fromString("4d1f13aa-7065-11ea-bc55-0242ac130003"))
                .name("user")
                .build();
    }

    public static Role createAdminRole() {
        return Role.builder()
                .id(UUID.fromString("65e65650-7065-11ea-bc55-0242ac130003"))
                .name("admin")
                .build();
    }

    public static List<Role> createRoleList() {
        final Role role1 = Role.builder()
                .id(UUID.fromString("65e65650-7065-11ea-bc55-0242ac130003"))
                .name("admin")
                .build();

        final Role role2 = Role.builder()
                .id(UUID.fromString("4d1f13aa-7065-11ea-bc55-0242ac130003"))
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }

    public static RoleDto createRoleResource() {
        return RoleDto.builder()
                .id("4d1f13aa-7065-11ea-bc55-0242ac130003")
                .name("user")
                .build();
    }

    public static RoleDto createRoleResourceAsNewRole() {
        return RoleDto.builder()
                .name("moderator")
                .build();
    }

    public static RoleDto createRoleResourceAsNewRoleResponse() {
        return RoleDto.builder()
                .id("4b8c17d6-f509-4569-8502-e7c2b0d8f13c")
                .name("moderator")
                .build();
    }

    public static List<RoleDto> createRoleResourceList() {
        final RoleDto role1 = RoleDto.builder()
                .id("65e65650-7065-11ea-bc55-0242ac130003")
                .name("admin")
                .build();

        final RoleDto role2 = RoleDto.builder()
                .id("4d1f13aa-7065-11ea-bc55-0242ac130003")
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }

    public static RoleDto createRoleResourceNegativeFlow() {
        return RoleDto.builder()
                .name("user")
                .build();
    }
}
