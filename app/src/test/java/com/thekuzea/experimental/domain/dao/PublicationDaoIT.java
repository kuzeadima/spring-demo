package com.thekuzea.experimental.domain.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.ttddyy.dsproxy.asserts.assertj.DataSourceAssertAssertions;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.thekuzea.experimental.util.DateTimeUtil.convertStringToOffsetDateTime;

@RunWith(Enclosed.class)
public class PublicationDaoIT {

    @DatabaseSetup(value = {
            "classpath:dataset/user-dataset.xml",
            "classpath:dataset/publication-dataset.xml"
    })
    public static class RunWithPreparedStatements extends DaoIT {

        @Autowired
        private PublicationRepository publicationRepository;

        @Test
        public void shouldFindAllPublications() {
            final List<Publication> publications = publicationRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publications).hasSize(3);
        }

        @Test
        public void shouldFindAllPublicationsByPublishedBy() {
            final User user = findUser(entityManager, "Larry");

            final List<Publication> publications = publicationRepository.findAllByPublishedBy(user);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(2);
            assertThat(publications).hasSize(2);
        }

        @Test
        public void shouldFindPublicationByTopic() {
            final String topic = "Hieracium venosum L.";
            final Optional<Publication> publication = publicationRepository.findByTopic(topic);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publication).isNotEmpty();
        }

        @Test
        public void shouldExistPublicationByTopic() {
            final String topic = "Hieracium venosum L.";
            final boolean publicationExists = publicationRepository.existsByTopic(topic);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publicationExists).isTrue();
        }

        @Test
        public void shouldNotSavePublication() {
            final User user = findUser(entityManager, "Kate");
            final Publication publication = Publication.builder()
                    .publishedBy(user)
                    .publicationTime(convertStringToOffsetDateTime("2020-02-26T14:21:43.298Z"))
                    .topic("Collinsia heterophylla Buist ex Graham")
                    .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                    .build();

            publicationRepository.save(publication);
            assertThatThrownBy(() -> entityManager.flush())
                    .isInstanceOf(PersistenceException.class);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(user.getId()).isNotNull();
        }

        @Test
        public void shouldDeletePublicationByTopic() {
            final String topic = "Hieracium venosum L.";

            publicationRepository.deleteByTopic(topic);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(1);
        }
    }

    public static class RunWithoutPreparedStatements extends DaoIT {

        @Autowired
        private PublicationRepository publicationRepository;

        @Test
        public void shouldNotFindAllPublications() {
            final List<Publication> publications = publicationRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publications).isEmpty();
        }

        @Test
        public void shouldNotFindAllPublicationsByPublishedBy() {
            final User user = findUser(entityManager, "root");

            final List<Publication> publications = publicationRepository.findAllByPublishedBy(user);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(2);
            assertThat(publications).isEmpty();
        }

        @Test
        public void shouldNotFindPublicationByTopic() {
            final String topic = "testTopic";

            final Optional<Publication> publication = publicationRepository.findByTopic(topic);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publication).isEmpty();
        }

        @Test
        public void shouldNotExistPublicationByTopic() {
            final String topic = "testTopic";

            final boolean publicationExists = publicationRepository.existsByTopic(topic);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(publicationExists).isFalse();
        }

        @Test
        public void shouldSavePublication() {
            final User user = findUser(entityManager, "user");
            final Publication publication = Publication.builder()
                    .publishedBy(user)
                    .publicationTime(convertStringToOffsetDateTime("2020-02-26T14:21:43.298Z"))
                    .topic("Achillea alpina L.")
                    .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                    .build();

            publicationRepository.save(publication);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(user.getId()).isNotNull();
        }

        @Test
        public void shouldNotDeletePublicationByTopic() {
            publicationRepository.deleteByTopic("testTopic");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(0);
        }
    }

    private static User findUser(final TestEntityManager entityManager, final String username) {
        return entityManager.getEntityManager()
                .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
