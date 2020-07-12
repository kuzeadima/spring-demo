package com.thekuzea.experimental.domain.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.ttddyy.dsproxy.asserts.assertj.DataSourceAssertAssertions;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.thekuzea.experimental.domain.model.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Enclosed.class)
public class RoleDaoIT {

    @DatabaseSetup(
            value = "classpath:dataset/clean-dataset.xml",
            type = DatabaseOperation.DELETE_ALL
    )
    public static class RunWithCleanPreparedStatements extends DaoIT {

        @Autowired
        private RoleRepository roleRepository;

        @Test
        public void shouldNotFindAllRoles() {
            final List<Role> roles = roleRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(roles).isEmpty();
        }

        @Test
        @DatabaseSetup(value = "classpath:dataset/role-dataset.xml")
        public void shouldDeleteRoleByName() {
            roleRepository.deleteByName("customRole");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(1);
        }
    }

    public static class RunWithoutPreparedStatements extends DaoIT {

        @Autowired
        private RoleRepository roleRepository;

        @Test
        public void shouldFindAllRoles() {
            final List<Role> roles = roleRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(roles).hasSize(2);
        }

        @Test
        public void shouldFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("admin");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(role).isNotEmpty();
        }

        @Test
        public void shouldNotFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("moderator");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(role).isEmpty();
        }

        @Test
        public void shouldNotFindNonExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("moderator");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByName).isFalse();
        }

        @Test
        public void shouldFindExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("admin");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByName).isTrue();
        }

        @Test
        public void shouldSaveRole() {
            final Role role = Role.builder()
                    .name("sys_admin")
                    .build();

            roleRepository.save(role);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(role.getId()).isNotNull();
        }

        @Test
        public void shouldNotSaveRole() {
            final Role role = Role.builder()
                    .name("admin")
                    .build();

            roleRepository.save(role);
            assertThatThrownBy(() -> entityManager.flush())
                    .isInstanceOf(PersistenceException.class);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(role.getId()).isNotNull();
        }

        @Test
        public void shouldNotDeleteRoleByName() {
            roleRepository.deleteByName("moderator");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(0);
        }
    }
}
