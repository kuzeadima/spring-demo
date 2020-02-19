package com.thekuzea.experimental.service.impl;

import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.exception.RoleNotFoundException;
import com.thekuzea.experimental.mapper.impl.RoleMapper;
import com.thekuzea.experimental.service.RoleService;
import com.thekuzea.experimental.constant.RoleMessages;
import com.thekuzea.experimental.logging.LoggingMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public List<RoleResource> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleResource addNewRole(final RoleResource roleResource) {
        final boolean roleExists = roleRepository.existsByName(roleResource.getName());

        if (!roleExists) {
            final Role mappedUser = roleMapper.mapToModel(roleResource);
            roleRepository.save(mappedUser);
        } else {
            roleResource.getValidationMessages().add(RoleMessages.ROLE_ALREADY_EXISTS);
            log.debug(LoggingMessages.ROLE_ALREADY_EXISTS_LOG, roleResource.getName());
        }

        return roleResource;
    }

    @Override
    @Transactional
    public RoleResource deleteByName(final String name) throws RoleNotFoundException {
        final Optional<Role> role = roleRepository.findByName(name);

        if (role.isPresent()) {
            roleRepository.deleteByName(name);
            return roleMapper.mapToDto(role.get());
        } else {
            log.debug(LoggingMessages.ROLE_NOT_FOUND_BY_NAME, name);
            throw new RoleNotFoundException();
        }
    }
}
