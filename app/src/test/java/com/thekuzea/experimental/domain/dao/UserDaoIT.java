package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.util.UserTestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(Enclosed.class)
public class UserDaoIT {

    public static class RunWithPreparedStatementsForDB extends DaoIT {

        @Autowired
        private UserRepository userRepository;

        @Before
        public void setUp() {
            UserTestDataGenerator.createUserList().forEach(entityManager::persist);
            entityManager.flush();
        }

        @Test
        public void shouldFindUserByUsername() {
            final Optional<User> user = userRepository.findByUsername("userN2");

            assertThat(user).isNotEmpty();
        }

        @Test
        public void shouldFindExistingUserByUsername() {
            final boolean existsByUsername = userRepository.existsByUsername("userN2");

            assertThat(existsByUsername).isTrue();
        }

        @Test
        public void shouldDeleteUserByUsername() {
            final String username = "userN2";

            userRepository.deleteByUsername("userN2");

            assertThrows(NoResultException.class, () -> entityManager.getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        }
    }

    public static class RunWithoutPreparedStatementsForDB extends DaoIT {

        @Autowired
        private UserRepository userRepository;

        @Test
        public void shouldNotFindUserByUsername() {
            final Optional<User> user = userRepository.findByUsername("userN5");

            assertThat(user).isEmpty();
        }

        @Test
        public void shouldNotFindNonExistingUserByUsername() {
            final boolean existsByUsername = userRepository.existsByUsername("userN5");

            assertThat(existsByUsername).isFalse();
        }
    }
}
