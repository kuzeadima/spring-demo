package com.thekuzea.experimental.domain.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.RepositoryDefinition;

import com.thekuzea.experimental.domain.model.Role;

@RepositoryDefinition(domainClass = Role.class, idClass = UUID.class)
public interface RoleRepository {

    List<Role> findAll();

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    void save(Role role);

    void deleteByName(String name);
}
