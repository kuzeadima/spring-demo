package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.domain.dto.UserDto;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getByUsername(String username);

    UserDto addNewUser(UserDto userDto);

    UserDto updateByUserId(String userId, UserDto userDto);

    void deleteByUsername(String username);
}
