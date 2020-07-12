package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.mapper.impl.UserMapper;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.service.impl.DefaultUserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.constant.messages.entity.UserMessages.USERNAME_IS_ALREADY_USED;
import static com.thekuzea.experimental.constant.messages.entity.UserMessages.USER_ALREADY_EXISTS;
import static com.thekuzea.experimental.constant.messages.entity.UserMessages.USER_NOT_FOUND;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createUserRole;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUser;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserList;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceForSave;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String DEFAULT_ROLE = "user";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        userService = new DefaultUserService(userRepository, roleRepository, userMapper, bCryptPasswordEncoder);
        ReflectionTestUtils.setField(userService, "defaultRole", DEFAULT_ROLE);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper, bCryptPasswordEncoder);
    }

    @Test
    public void shouldGetAllUsers() {
        final List<User> userList = createUserList();
        when(userRepository.findAll()).thenReturn(userList);

        final List<UserDto> actualResourceList = userService.getAllUsers();

        verify(userRepository).findAll();
        verify(userMapper, times(3)).mapToDto(any(User.class));
        assertThat(actualResourceList.size()).isEqualTo(3);
    }

    @Test
    public void shouldNotGetAllUsers() {
        final List<User> userList = Collections.emptyList();
        when(userRepository.findAll()).thenReturn(userList);

        final List<UserDto> actualResourceList = userService.getAllUsers();

        verify(userRepository).findAll();
        verify(userMapper, times(0)).mapToDto(any(User.class));
        assertThat(actualResourceList).isEmpty();
    }

    @Test
    public void shouldGetUserByUsername() {
        final UserDto expectedUserDto = createUserResource();
        final User expectedUser = createUser();
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));
        when(userMapper.mapToDto(expectedUser)).thenReturn(expectedUserDto);

        final UserDto actualUserDto = userService.getByUsername(expectedUser.getUsername());

        verify(userRepository).findByUsername(expectedUser.getUsername());
        verify(userMapper).mapToDto(expectedUser);
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    public void shouldNotGetUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByUsername(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findByUsername(username);
    }

    @Test
    public void shouldAddNewUser() {
        final UserDto expectedUserDto = createUserResourceForSave();
        final User expectedUser = createUser();
        final Role role = createUserRole();
        final String username = expectedUserDto.getUsername();
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(userMapper.mapToModel(expectedUserDto)).thenReturn(expectedUser);

        final UserDto actualUserDto = userService.addNewUser(expectedUserDto);

        verify(userRepository).existsByUsername(username);
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(userMapper).mapToModel(expectedUserDto);
        verify(bCryptPasswordEncoder).encode(expectedUserDto.getPassword());
        verify(userRepository).save(expectedUser);
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    public void shouldNotAddNewUser() {
        final UserDto expectedUserDto = createUserResource();
        final User expectedUser = createUser();
        final Role role = createUserRole();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(true);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));

        assertThatThrownBy(() -> userService.addNewUser(expectedUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_ALREADY_EXISTS);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    public void shouldNotAddNewUserRoleNotFound() {
        final UserDto expectedUserDto = createUserResource();
        final User expectedUser = createUser();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.addNewUser(expectedUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DEFAULT_ROLE_NOT_FOUND);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    public void shouldUpdateUserByUserId() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto expectedUserDto = createUserResourceForSave();
        final User user = createUser();
        final Role role = createUserRole();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(expectedUserDto.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(userMapper.mapToDto(user)).thenReturn(expectedUserDto);

        final UserDto actualUserDto = userService.updateByUserId(id, expectedUserDto);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(expectedUserDto.getUsername());
        verify(bCryptPasswordEncoder).encode(expectedUserDto.getPassword());
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(userRepository).save(user);
        verify(userMapper).mapToDto(user);
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    public void shouldNotUpdateUserByUserIdReasonRoleNotFound() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserResource();
        final User user = createUser();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_NOT_FOUND);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(userDto.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    public void shouldNotUpdateUserByUserIdReasonUserNotFound() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserResource();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findById(UUID.fromString(id));
    }

    @Test
    public void shouldNotUpdateUserByUserIdReasonUsernameIsAlreadyUsed() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserResource();
        final User user = createUser();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USERNAME_IS_ALREADY_USED);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(userDto.getUsername());
    }

    @Test
    public void shouldDeleteUserByUsername() {
        final User expectedUser = createUser();
        final String username = expectedUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        userService.deleteByUsername(username);

        verify(userRepository).findByUsername(username);
        verify(userRepository).deleteByUsername(username);
    }

    @Test
    public void shouldNotDeleteUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteByUsername(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findByUsername(username);
    }
}
