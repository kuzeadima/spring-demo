package com.thekuzea.experimental.config.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.thekuzea.experimental.constant.messages.entity.UserMessages;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceAsNewUser;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceAsNewUserResponse;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceAsUpdateUserResponse;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceList;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceNegativeFlowDefaultRoleNotFound;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceNegativeFlowUserAlreadyExists;

@TestConfiguration
public class UserServiceMockConfig {

    @Bean
    @Primary
    public UserService userService() {
        final UserService userService = mock(UserService.class);

        when(userService.getAllUsers()).thenReturn(createUserResourceList());
        when(userService.getByUsername("Larry")).thenReturn(createUserResource());
        when(userService.getByUsername("Alice")).thenThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND));
        doThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND)).when(userService).deleteByUsername("Alice");

        when(userService.addNewUser(refEq(createUserResourceAsNewUser())))
                .thenReturn(createUserResourceAsNewUserResponse());
        when(userService.addNewUser(refEq(createUserResourceNegativeFlowUserAlreadyExists())))
                .thenThrow(new IllegalArgumentException(UserMessages.USER_ALREADY_EXISTS));
        when(userService.addNewUser(refEq(createUserResourceNegativeFlowDefaultRoleNotFound())))
                .thenThrow(new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND));

        when(userService.updateByUserId(eq("afa06be6-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenReturn(createUserResourceAsUpdateUserResponse());
        when(userService.updateByUserId(eq("762eb61a-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(ROLE_NOT_FOUND));
        when(userService.updateByUserId(eq("e158aaa4-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND));
        when(userService.updateByUserId(eq("84db923c-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(UserMessages.USERNAME_IS_ALREADY_USED));

        return userService;
    }
}
