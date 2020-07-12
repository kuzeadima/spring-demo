package com.thekuzea.experimental.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.constant.messages.entity.RoleMessages;
import com.thekuzea.experimental.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.mapper.impl.RoleMapper;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.service.RoleService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDto addNewRole(final RoleDto roleDto) {
        final boolean roleExists = roleRepository.existsByName(roleDto.getName());

        if (!roleExists) {
            final Role mappedRole = roleMapper.mapToModel(roleDto);
            roleRepository.save(mappedRole);

            log.debug(LoggingMessages.SAVED_NEW_ROLE, mappedRole.getName());
        } else {
            log.debug(LoggingMessages.ROLE_ALREADY_EXISTS_LOG, roleDto.getName());
            throw new IllegalArgumentException(RoleMessages.ROLE_ALREADY_EXISTS);
        }

        return roleDto;
    }

    @Override
    @Transactional
    public void deleteByName(final String name) {
        final Optional<Role> possibleRole = roleRepository.findByName(name);

        if (possibleRole.isPresent()) {
            roleRepository.deleteByName(name);
            log.debug(LoggingMessages.DELETED_ROLE, name);
        } else {
            log.debug(LoggingMessages.ROLE_NOT_FOUND_BY_NAME, name);
            throw new IllegalArgumentException(RoleMessages.ROLE_NOT_FOUND);
        }
    }
}
