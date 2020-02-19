package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.User;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = User.class, idClass = UUID.class)
public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void save(User user);

    void deleteByUsername(String username);
}
