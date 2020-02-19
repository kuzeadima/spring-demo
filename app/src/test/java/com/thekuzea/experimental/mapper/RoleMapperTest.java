package com.thekuzea.experimental.mapper;

import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.mapper.impl.RoleMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRole;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RoleMapperTest {

    private RoleMapper roleMapper;

    @Before
    public void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    public void shouldMapDtoToModel() {
        final RoleResource userResource = createRoleResource();
        final Role expectedUserModel = createRole();

        final Role actualUserModel = roleMapper.mapToModel(userResource);

        assertThat(expectedUserModel.getName()).isEqualTo(actualUserModel.getName());
    }

    @Test
    public void shouldMapModelToDto() {
        final RoleResource expectedUserResource = createRoleResource();
        final Role userModel = createRole();

        final RoleResource actualUserResource = roleMapper.mapToDto(userModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(expectedUserResource.getId()).isEqualTo(actualUserResource.getId());
            softly.assertThat(expectedUserResource.getName()).isEqualTo(actualUserResource.getName());
        });
    }
}
