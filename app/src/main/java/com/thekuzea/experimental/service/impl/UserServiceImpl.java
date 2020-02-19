package com.thekuzea.experimental.service.impl;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.exception.UserNotFoundException;
import com.thekuzea.experimental.mapper.impl.UserMapper;
import com.thekuzea.experimental.service.UserService;
import com.thekuzea.experimental.constant.RoleMessages;
import com.thekuzea.experimental.constant.UserMessages;
import com.thekuzea.experimental.logging.LoggingMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${default.role}")
    private String defaultRole;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserResource> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResource getByUsername(final String username) throws UserNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return userMapper.mapToDto(user.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public UserResource addNewUser(final UserResource userResource) {
        final boolean userExists = userRepository.existsByUsername(userResource.getUsername());
        final Optional<Role> foundRole = roleRepository.findByName(defaultRole);

        if (!userExists) {
            if (foundRole.isPresent()) {
                final User mappedUser = userMapper.mapToModel(userResource);
                mappedUser.setRole(foundRole.get());

                userRepository.save(mappedUser);
            } else {
                userResource.getValidationMessages().add(RoleMessages.ROLE_NOT_FOUND);
                log.error(LoggingMessages.ROLE_NOT_FOUND_BY_NAME, defaultRole);
            }
        } else {
            userResource.getValidationMessages().add(UserMessages.USER_ALREADY_EXISTS);
            log.debug(LoggingMessages.USER_ALREADY_EXISTS_LOG, userResource.getUsername());
        }

        return userResource;
    }

    @Override
    @Transactional
    public UserResource updateByUserId(final String userId,
                                       final UserResource userResource) throws UserNotFoundException {

        final Optional<User> foundUser = userRepository.findById(userId);
        boolean isValidationSuccessful = false;

        if (foundUser.isPresent()) {
            final User user = foundUser.get();
            final Optional<Role> possibleRole = roleRepository.findByName(userResource.getRole());

            if (possibleRole.isPresent()) {
                user.setUsername(userResource.getUsername());
                user.setPassword(userResource.getPassword());
                user.setRole(possibleRole.get());
                userRepository.save(user);

                isValidationSuccessful = true;
            } else {
                userResource.getValidationMessages().add(RoleMessages.ROLE_NOT_FOUND);
            }

            if (isValidationSuccessful) {
                return userMapper.mapToDto(foundUser.get());
            } else {
                return userResource;
            }
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_ID, userId);
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public UserResource deleteByUsername(final String username) throws UserNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            userRepository.deleteByUsername(username);
            return userMapper.mapToDto(user.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new UserNotFoundException();
        }
    }
}
