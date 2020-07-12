package com.thekuzea.experimental.domain.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.RepositoryDefinition;

import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;

@RepositoryDefinition(domainClass = Publication.class, idClass = UUID.class)
public interface PublicationRepository {

    List<Publication> findAll();

    List<Publication> findAllByPublishedBy(User publishedBy);

    Optional<Publication> findByTopic(String topic);

    boolean existsByTopic(String topic);

    void save(Publication publication);

    void deleteByTopic(String topic);
}
