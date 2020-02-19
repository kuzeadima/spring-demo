package com.thekuzea.experimental.util;

import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.domain.dto.UserResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRole;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;

public class UserTestDataGenerator {

    public static User createUser() {
        return User.builder()
                .username("userN1")
                .password("password1")
                .build();
    }

    public static User createUserForMapper() {
        return User.builder()
                .id(UUID.fromString("00000001-0001-0001-0001-000000000001"))
                .username("userN1")
                .password("password1")
                .role(createRole())
                .build();
    }

    public static List<User> createUserList() {
        final User user1 = User.builder()
                .username("userN1")
                .password("password1")
                .build();

        final User user2 = User.builder()
                .username("userN2")
                .password("password2")
                .build();

        return Arrays.asList(user1, user2);
    }

    public static UserResource createUserResource() {
        return UserResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .username("userN1")
                .role(createRoleResource().getName())
                .build();
    }

    public static UserResource createUserResourceForMapper() {
        return UserResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .username("userN1")
                .password("password1")
                .build();
    }

    public static List<UserResource> createUserResourceList() {
        final UserResource user1 = UserResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .username("userN1")
                .build();

        final UserResource user2 = UserResource.builder()
                .id("00000002-0002-0002-0002-000000000002")
                .username("userN2")
                .build();

        return Arrays.asList(user1, user2);
    }

    public static UserResource createUserResourceWithErrorMessage(final String message) {
        final List<String> validationMessage = new ArrayList<>();
        validationMessage.add(message);

        return UserResource.builder()
                .id("00000001-0001-0001-0001-000000000001")
                .username("userN1")
                .validationMessages(validationMessage)
                .build();
    }
}
