package com.thekuzea.experimental.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.constant.messages.entity.UserMessages;
import com.thekuzea.experimental.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.mapper.impl.UserMapper;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.service.UserService;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    @Value("${security.settings.default-role}")
    private String defaultRole;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getByUsername(final String username) {
        final Optional<User> possibleUser = userRepository.findByUsername(username);

        if (possibleUser.isPresent()) {
            return userMapper.mapToDto(possibleUser.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public UserDto addNewUser(final UserDto userDto) {
        final boolean userExists = userRepository.existsByUsername(userDto.getUsername());
        final Optional<Role> foundRole = roleRepository.findByName(defaultRole);

        if (!userExists) {
            if (foundRole.isPresent()) {
                final User mappedUser = userMapper.mapToModel(userDto);
                mappedUser.setPassword(bCryptPasswordEncoder.encode(mappedUser.getPassword()));
                mappedUser.setRole(foundRole.get());

                userRepository.save(mappedUser);
                log.debug(LoggingMessages.SAVED_NEW_USER, mappedUser.getId());
            } else {
                log.error(LoggingMessages.ROLE_NOT_FOUND_BY_NAME, defaultRole);
                throw new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND);
            }
        } else {
            log.debug(LoggingMessages.USER_ALREADY_EXISTS_LOG, userDto.getUsername());
            throw new IllegalArgumentException(UserMessages.USER_ALREADY_EXISTS);
        }

        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateByUserId(final String userId, final UserDto userDto) {
        final UUID id = UUID.fromString(userId);
        final Optional<User> possibleUser = userRepository.findById(id);

        if (possibleUser.isPresent()) {
            final User user = possibleUser.get();
            modifyUser(user, userDto);

            userRepository.save(user);
            log.debug(LoggingMessages.UPDATED_USER, userId);

            return userMapper.mapToDto(possibleUser.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_ID, userId);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteByUsername(final String username) {
        final Optional<User> possibleUser = userRepository.findByUsername(username);

        if (possibleUser.isPresent()) {
            userRepository.deleteByUsername(username);
            log.debug(LoggingMessages.DELETED_USER, username);
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    private void modifyUser(final User user, final UserDto userDto) {
        if (!isBlank(userDto.getUsername())) {
            final String providedUsername = userDto.getUsername();
            final Optional<User> possibleUserByUsername = userRepository.findByUsername(providedUsername);

            if (possibleUserByUsername.isPresent()) {
                throw new IllegalArgumentException(UserMessages.USERNAME_IS_ALREADY_USED);
            }
            user.setUsername(providedUsername);
        }

        if (!isBlank(userDto.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }

        if (!isBlank(userDto.getRole())) {
            final Optional<Role> possibleRole = roleRepository.findByName(userDto.getRole());

            if (possibleRole.isPresent()) {
                user.setRole(possibleRole.get());
            } else {
                throw new IllegalArgumentException(ROLE_NOT_FOUND);
            }
        }
    }
}
