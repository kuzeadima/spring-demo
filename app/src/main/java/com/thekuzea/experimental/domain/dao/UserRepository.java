package com.thekuzea.experimental.domain.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.RepositoryDefinition;

import com.thekuzea.experimental.domain.model.User;

@RepositoryDefinition(domainClass = User.class, idClass = UUID.class)
public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void save(User user);

    void deleteByUsername(String username);
}
