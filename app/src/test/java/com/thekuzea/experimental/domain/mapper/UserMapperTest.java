package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.mapper.impl.UserMapper;
import com.thekuzea.experimental.domain.model.User;

import static com.thekuzea.experimental.util.UserTestDataGenerator.createUser;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceForMapper;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {

    private UserMapper userMapper;

    @Before
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void shouldMapDtoToModel() {
        final UserDto userDto = createUserResourceForMapper();
        final User expectedUserModel = createUser();

        final User actualUserModel = userMapper.mapToModel(userDto);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualUserModel.getUsername()).isEqualTo(expectedUserModel.getUsername());
            softly.assertThat(actualUserModel.getPassword()).isEqualTo(expectedUserModel.getPassword());
        });
    }

    @Test
    public void shouldMapModelToDto() {
        final UserDto expectedUserDto = createUserResource();
        final User userModel = createUser();

        final UserDto actualUserDto = userMapper.mapToDto(userModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualUserDto.getId()).isEqualTo(expectedUserDto.getId());
            softly.assertThat(actualUserDto.getUsername()).isEqualTo(expectedUserDto.getUsername());
            softly.assertThat(actualUserDto.getPassword()).isNull();
            softly.assertThat(actualUserDto.getRole()).isEqualTo(expectedUserDto.getRole());
        });
    }
}
