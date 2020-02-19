package com.thekuzea.experimental.service;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.exception.UserNotFoundException;
import com.thekuzea.experimental.mapper.impl.UserMapper;
import com.thekuzea.experimental.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.thekuzea.experimental.constant.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.constant.UserMessages.USER_ALREADY_EXISTS;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRole;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUser;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserList;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private static final String DEFAULT_ROLE = "user";

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserMapper userMapper;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, userMapper);
        ReflectionTestUtils.setField(userService, "defaultRole", DEFAULT_ROLE);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper);
    }

    @Test
    public void shouldGetAllUsers() {
        final List<User> userList = createUserList();
        when(userRepository.findAll()).thenReturn(userList);

        final List<UserResource> actualResourceList = userService.getAllUsers();

        verify(userRepository).findAll();
        verify(userMapper, times(2)).mapToDto(any(User.class));
        assertThat(actualResourceList.size()).isEqualTo(2);
    }

    @Test
    public void shouldGetUserByUsername() throws UserNotFoundException {
        final UserResource expectedUserResource = createUserResource();
        final User expectedUser = createUser();
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));
        when(userMapper.mapToDto(expectedUser)).thenReturn(expectedUserResource);

        final UserResource actualUserResource = userService.getByUsername(expectedUser.getUsername());

        verify(userRepository).findByUsername(expectedUser.getUsername());
        verify(userMapper).mapToDto(expectedUser);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
    }

    @Test
    public void shouldNotGetUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByUsername(username));

        verify(userRepository).findByUsername(username);
    }

    @Test
    public void shouldAddNewUser() {
        final UserResource expectedUserResource = createUserResource();
        final User expectedUser = createUser();
        final Role role = createRole();
        final String username = expectedUserResource.getUsername();
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(userMapper.mapToModel(expectedUserResource)).thenReturn(expectedUser);

        final UserResource actualUserResource = userService.addNewUser(expectedUserResource);

        verify(userRepository).existsByUsername(username);
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(userMapper).mapToModel(expectedUserResource);
        verify(userRepository).save(expectedUser);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
    }

    @Test
    public void shouldNotAddNewUser() {
        final UserResource expectedUserResource = createUserResource();
        final User expectedUser = createUser();
        final Role role = createRole();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(true);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));

        final UserResource actualUserResource = userService.addNewUser(expectedUserResource);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
        assertThat(actualUserResource.getValidationMessages()).isNotEmpty();
        assertThat(actualUserResource.getValidationMessages()).contains(USER_ALREADY_EXISTS);
    }

    @Test
    public void shouldNotAddNewUserRoleNotFound() {
        final UserResource expectedUserResource = createUserResource();
        final User expectedUser = createUser();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        final UserResource actualUserResource = userService.addNewUser(expectedUserResource);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
        assertThat(actualUserResource.getValidationMessages()).isNotEmpty();
        assertThat(actualUserResource.getValidationMessages()).contains(ROLE_NOT_FOUND);
    }

    @Test
    public void shouldUpdateUserByUserId() throws UserNotFoundException {
        final String id = "userId";
        final UserResource expectedUserResource = createUserResource();
        final User user = createUser();
        final Role role = createRole();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(userMapper.mapToDto(user)).thenReturn(expectedUserResource);

        final UserResource actualUserResource = userService.updateByUserId(id, expectedUserResource);

        verify(userRepository).findById(id);
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(userRepository).save(user);
        verify(userMapper).mapToDto(user);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
    }

    @Test
    public void shouldNotUpdateUserByUserIdAndPopulateValidationMessages() throws UserNotFoundException {
        final String id = "userId";
        final UserResource userResource = createUserResource();
        final User user = createUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        final UserResource actualUserResource = userService.updateByUserId(id, userResource);

        verify(userRepository).findById(id);
        verify(roleRepository).findByName(DEFAULT_ROLE);
        assertThat(actualUserResource.getValidationMessages()).contains(ROLE_NOT_FOUND);
    }

    @Test
    public void shouldNotUpdateUserByUserIdAndThrowException() {
        final String id = "userId";
        final UserResource userResource = createUserResource();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateByUserId(id, userResource));

        verify(userRepository).findById(id);
    }

    @Test
    public void shouldDeleteUserByUsername() throws UserNotFoundException {
        final UserResource expectedUserResource = createUserResource();
        final User expectedUser = createUser();
        final String username = expectedUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
        when(userMapper.mapToDto(expectedUser)).thenReturn(expectedUserResource);

        final UserResource actualUserResource = userService.deleteByUsername(username);

        verify(userRepository).findByUsername(username);
        verify(userMapper).mapToDto(expectedUser);
        verify(userRepository).deleteByUsername(username);
        assertThat(expectedUserResource).isEqualTo(actualUserResource);
    }

    @Test
    public void shouldNotDeleteUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteByUsername(username));

        verify(userRepository).findByUsername(username);
    }
}
