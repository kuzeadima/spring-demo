package com.thekuzea.experimental.service;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.exception.RoleNotFoundException;
import com.thekuzea.experimental.mapper.impl.RoleMapper;
import com.thekuzea.experimental.service.impl.RoleServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.thekuzea.experimental.constant.RoleMessages.ROLE_ALREADY_EXISTS;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRole;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleList;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RoleServiceTest {

    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RoleMapper roleMapper;

    @Before
    public void setUp() {
        roleService = new RoleServiceImpl(roleRepository, roleMapper);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(roleRepository, roleMapper);
    }

    @Test
    public void shouldGetAllRoles() {
        final List<Role> roleList = createRoleList();
        when(roleRepository.findAll()).thenReturn(roleList);

        final List<RoleResource> actualResourceList = roleService.getAllRoles();

        verify(roleRepository).findAll();
        verify(roleMapper, times(2)).mapToDto(any(Role.class));
        assertThat(actualResourceList.size()).isEqualTo(2);
    }

    @Test
    public void shouldAddNewUser() {
        final RoleResource expectedRoleResource = createRoleResource();
        final Role expectedRole = createRole();
        final String name = expectedRoleResource.getName();
        when(roleRepository.existsByName(name)).thenReturn(false);
        when(roleMapper.mapToModel(expectedRoleResource)).thenReturn(expectedRole);

        final RoleResource actualRoleResource = roleService.addNewRole(expectedRoleResource);

        verify(roleRepository).existsByName(name);
        verify(roleMapper).mapToModel(expectedRoleResource);
        verify(roleRepository).save(expectedRole);
        assertThat(expectedRoleResource).isEqualTo(actualRoleResource);
    }

    @Test
    public void shouldNotAddNewUser() {
        final RoleResource expectedRoleResource = createRoleResource();
        final Role expectedRole = createRole();
        when(roleRepository.existsByName(expectedRole.getName())).thenReturn(true);

        final RoleResource actualRoleResource = roleService.addNewRole(expectedRoleResource);

        verify(roleRepository).existsByName(expectedRole.getName());
        assertThat(expectedRoleResource).isEqualTo(actualRoleResource);
        assertThat(actualRoleResource.getValidationMessages()).isNotEmpty();
        assertThat(actualRoleResource.getValidationMessages()).contains(ROLE_ALREADY_EXISTS);
    }

    @Test
    public void shouldDeleteUserByUsername() throws RoleNotFoundException {
        final RoleResource expectedRoleResource = createRoleResource();
        final Role expectedRole = createRole();
        final String name = expectedRole.getName();
        when(roleRepository.findByName(name)).thenReturn(Optional.of(expectedRole));
        when(roleMapper.mapToDto(expectedRole)).thenReturn(expectedRoleResource);

        final RoleResource actualRoleResource = roleService.deleteByName(name);

        verify(roleRepository).findByName(name);
        verify(roleMapper).mapToDto(expectedRole);
        verify(roleRepository).deleteByName(name);
        assertThat(expectedRoleResource).isEqualTo(actualRoleResource);
    }

    @Test
    public void shouldNotDeleteUserByUsername() {
        final String name = "unknown";
        when(roleRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.deleteByName(name));

        verify(roleRepository).findByName(name);
    }
}
