package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.mapper.impl.RoleMapper;
import com.thekuzea.experimental.domain.model.Role;

import static org.assertj.core.api.Assertions.assertThat;

import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createUserRole;

@RunWith(MockitoJUnitRunner.class)
public class RoleMapperTest {

    private RoleMapper roleMapper;

    @Before
    public void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    public void shouldMapDtoToModel() {
        final RoleDto roleDto = createRoleResource();
        final Role expectedRoleModel = createUserRole();

        final Role actualRoleModel = roleMapper.mapToModel(roleDto);

        assertThat(actualRoleModel.getName()).isEqualTo(expectedRoleModel.getName());
    }

    @Test
    public void shouldMapModelToDto() {
        final RoleDto expectedRoleDto = createRoleResource();
        final Role roleModel = createUserRole();

        final RoleDto actualRoleDto = roleMapper.mapToDto(roleModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualRoleDto.getId()).isEqualTo(expectedRoleDto.getId());
            softly.assertThat(actualRoleDto.getName()).isEqualTo(expectedRoleDto.getName());
        });
    }
}
