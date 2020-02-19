package com.thekuzea.experimental.mapper;

import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.mapper.impl.UserMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.thekuzea.experimental.util.UserTestDataGenerator.createUser;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserForMapper;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceForMapper;

@RunWith(SpringRunner.class)
public class UserMapperTest {

    private UserMapper userMapper;

    @Before
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void shouldMapDtoToModel() {
        final UserResource userResource = createUserResourceForMapper();
        final User expectedUserModel = createUser();

        final User actualUserModel = userMapper.mapToModel(userResource);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(expectedUserModel.getUsername()).isEqualTo(actualUserModel.getUsername());
            softly.assertThat(expectedUserModel.getPassword()).isEqualTo(actualUserModel.getPassword());
        });
    }

    @Test
    public void shouldMapModelToDto() {
        final UserResource expectedUserResource = createUserResource();
        final User userModel = createUserForMapper();

        final UserResource actualUserResource = userMapper.mapToDto(userModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(expectedUserResource.getId()).isEqualTo(actualUserResource.getId());
            softly.assertThat(expectedUserResource.getUsername()).isEqualTo(actualUserResource.getUsername());
            softly.assertThat(expectedUserResource.getPassword()).isEqualTo(actualUserResource.getPassword());
            softly.assertThat(expectedUserResource.getRole()).isEqualTo(actualUserResource.getRole());
        });
    }
}
