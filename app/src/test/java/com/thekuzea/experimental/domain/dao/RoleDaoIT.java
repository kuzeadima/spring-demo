package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;
import java.util.Optional;

import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(Enclosed.class)
public class RoleDaoIT {

    public static class RunWithPreparedStatementsForDB extends DaoIT {

        @Autowired
        private RoleRepository roleRepository;

        @Before
        public void setUp() {
            createRoleList().forEach(entityManager::persist);
            entityManager.flush();
        }

        @Test
        public void shouldFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("admin");

            assertThat(role).isNotEmpty();
        }

        @Test
        public void shouldFindExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("admin");

            assertThat(existsByName).isTrue();
        }

        @Test
        public void shouldDeleteRoleByName() {
            final String name = "user";

            roleRepository.deleteByName(name);

            assertThrows(NoResultException.class, () -> entityManager.getEntityManager()
                    .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult());
        }
    }

    public static class RunWithoutPreparedStatementsForDB extends DaoIT {

        @Autowired
        private RoleRepository roleRepository;

        @Test
        public void shouldNotFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("moderator");

            assertThat(role).isEmpty();
        }

        @Test
        public void shouldNotFindNonExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("moderator");

            assertThat(existsByName).isFalse();
        }
    }
}
