package com.thekuzea.experimental.service;

import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserResource> getAllUsers();

    UserResource getByUsername(String username) throws UserNotFoundException;

    UserResource addNewUser(UserResource userResource);

    UserResource updateByUserId(String userId, UserResource userResource) throws UserNotFoundException;

    UserResource deleteByUsername(String username) throws UserNotFoundException;
}
