package com.thekuzea.experimental.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTestDataGenerator {

    public static User createUser() {
        return User.builder()
                .id(UUID.fromString("762eb61a-7065-11ea-bc55-0242ac130003"))
                .username("Larry")
                .password("password123")
                .role(RoleTestDataGenerator.createUserRole())
                .build();
    }

    public static List<User> createUserList() {
        final User user1 = User.builder()
                .id(UUID.fromString("84db923c-7065-11ea-bc55-0242ac130003"))
                .username("root")
                .password("root")
                .role(RoleTestDataGenerator.createAdminRole())
                .build();

        final User user2 = User.builder()
                .id(UUID.fromString("762eb61a-7065-11ea-bc55-0242ac130003"))
                .username("Larry")
                .password("password123")
                .role(RoleTestDataGenerator.createUserRole())
                .build();

        final User user3 = User.builder()
                .id(UUID.fromString("afa06be6-7065-11ea-bc55-0242ac130003"))
                .username("Kate")
                .password("password456")
                .role(RoleTestDataGenerator.createUserRole())
                .build();

        return Arrays.asList(user1, user2, user3);
    }

    public static UserDto createUserResource() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .role("user")
                .build();
    }

    public static UserDto createUserResourceForSave() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .password("password123")
                .role("user")
                .build();
    }

    public static UserDto createUserResourceAsUpdateUserResponse() {
        return UserDto.builder()
                .id("afa06be6-7065-11ea-bc55-0242ac130003")
                .username("Kate")
                .role("user")
                .build();
    }

    public static UserDto createUserResourceForMapper() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .password("password123")
                .build();
    }

    public static UserDto createUserResourceAsNewUser() {
        return UserDto.builder()
                .username("Nataly")
                .password("password123")
                .build();
    }

    public static UserDto createUserResourceAsNewUserResponse() {
        return UserDto.builder()
                .username("Nataly")
                .role("user")
                .build();
    }

    public static List<UserDto> createUserResourceList() {
        final UserDto user1 = UserDto.builder()
                .id("84db923c-7065-11ea-bc55-0242ac130003")
                .username("root")
                .role("admin")
                .build();

        final UserDto user2 = UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .role("user")
                .build();

        final UserDto user3 = UserDto.builder()
                .id("afa06be6-7065-11ea-bc55-0242ac130003")
                .username("Kate")
                .role("user")
                .build();

        return Arrays.asList(user1, user2, user3);
    }

    public static UserDto createUserResourceNegativeFlowUserAlreadyExists() {
        return UserDto.builder()
                .username("Larry")
                .password("password123")
                .build();
    }

    public static UserDto createUserResourceNegativeFlowDefaultRoleNotFound() {
        return UserDto.builder()
                .username("Julia")
                .password("password123")
                .build();
    }
}
