package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.mapper.impl.RoleMapper;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.service.impl.DefaultRoleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_ALREADY_EXISTS;
import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleList;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createUserRole;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Before
    public void setUp() {
        roleService = new DefaultRoleService(roleRepository, roleMapper);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(roleRepository, roleMapper);
    }

    @Test
    public void shouldGetAllRoles() {
        final List<Role> roleList = createRoleList();
        when(roleRepository.findAll()).thenReturn(roleList);

        final List<RoleDto> actualResourceList = roleService.getAllRoles();

        verify(roleRepository).findAll();
        verify(roleMapper, times(2)).mapToDto(any(Role.class));
        assertThat(actualResourceList.size()).isEqualTo(2);
    }

    @Test
    public void shouldNotGetAllRoles() {
        final List<Role> roleList = Collections.emptyList();
        when(roleRepository.findAll()).thenReturn(roleList);

        final List<RoleDto> actualResourceList = roleService.getAllRoles();

        verify(roleRepository).findAll();
        verify(roleMapper, times(0)).mapToDto(any(Role.class));
        assertThat(actualResourceList).isEmpty();
    }

    @Test
    public void shouldAddNewUser() {
        final RoleDto expectedRoleDto = createRoleResource();
        final Role expectedRole = createUserRole();
        final String name = expectedRoleDto.getName();
        when(roleRepository.existsByName(name)).thenReturn(false);
        when(roleMapper.mapToModel(expectedRoleDto)).thenReturn(expectedRole);

        final RoleDto actualRoleDto = roleService.addNewRole(expectedRoleDto);

        verify(roleRepository).existsByName(name);
        verify(roleMapper).mapToModel(expectedRoleDto);
        verify(roleRepository).save(expectedRole);
        assertThat(actualRoleDto).isEqualTo(expectedRoleDto);
    }

    @Test
    public void shouldNotAddNewUser() {
        final RoleDto expectedRoleDto = createRoleResource();
        final Role expectedRole = createUserRole();
        when(roleRepository.existsByName(expectedRole.getName())).thenReturn(true);

        assertThatThrownBy(() -> roleService.addNewRole(expectedRoleDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_ALREADY_EXISTS);

        verify(roleRepository).existsByName(expectedRole.getName());
    }

    @Test
    public void shouldDeleteUserByUsername() {
        final Role expectedRole = createUserRole();
        final String name = expectedRole.getName();
        when(roleRepository.findByName(name)).thenReturn(Optional.of(expectedRole));

        roleService.deleteByName(name);

        verify(roleRepository).findByName(name);
        verify(roleRepository).deleteByName(name);
    }

    @Test
    public void shouldNotDeleteUserByUsername() {
        final String name = "unknown";
        when(roleRepository.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.deleteByName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_NOT_FOUND);

        verify(roleRepository).findByName(name);
    }
}
