package com.thekuzea.experimental.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.domain.dao.PublicationRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.mapper.impl.PublicationMapper;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.service.AuthenticationService;
import com.thekuzea.experimental.service.PublicationService;

import static com.thekuzea.experimental.constant.messages.entity.PublicationMessages.PUBLICATION_ALREADY_EXISTS;
import static com.thekuzea.experimental.constant.messages.entity.PublicationMessages.PUBLICATION_NOT_FOUND;
import static com.thekuzea.experimental.constant.messages.entity.PublicationMessages.WRONG_PUBLISHER_PROVIDED;
import static com.thekuzea.experimental.constant.messages.logging.LoggingMessages.USER_NOT_FOUND_BY_USERNAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPublicationService implements PublicationService {

    private final PublicationRepository publicationRepository;

    private final UserRepository userRepository;

    private final PublicationMapper publicationMapper;

    private final AuthenticationService authenticationService;

    @Override
    public List<PublicationDto> getAllPublications() {
        return publicationRepository.findAll()
                .stream()
                .map(publicationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDto> getAllPublicationsForCurrentUser() {
        final UserDetails currentLoggedInUser = authenticationService.getCurrentLoggedInUser();
        final Optional<User> foundUser = userRepository.findByUsername(currentLoggedInUser.getUsername());
        List<PublicationDto> foundPublications = Collections.emptyList();

        if (foundUser.isPresent()) {
            foundPublications = publicationRepository.findAllByPublishedBy(foundUser.get())
                    .stream()
                    .map(publicationMapper::mapToDto)
                    .collect(Collectors.toList());
        }

        return foundPublications;
    }

    @Override
    @Transactional
    public PublicationDto addNewPublication(final PublicationDto publicationDto) {
        final boolean publicationExists = publicationRepository.existsByTopic(publicationDto.getTopic());

        if (!publicationExists) {
            final String currentLoggedInUser = authenticationService.getCurrentLoggedInUser().getUsername();
            final Optional<User> foundUser = userRepository.findByUsername(currentLoggedInUser);

            if (foundUser.isPresent()) {
                final Publication mappedEntity = publicationMapper.mapToModel(publicationDto);
                mappedEntity.setPublishedBy(foundUser.get());
                publicationRepository.save(mappedEntity);
            } else {
                log.error(USER_NOT_FOUND_BY_USERNAME, publicationDto.getPublishedBy());
                throw new IllegalArgumentException(WRONG_PUBLISHER_PROVIDED);
            }
        } else {
            log.debug(LoggingMessages.PUBLICATION_ALREADY_EXISTS_LOG, publicationDto.getTopic());
            throw new IllegalArgumentException(PUBLICATION_ALREADY_EXISTS);
        }

        return publicationDto;
    }

    @Override
    @Transactional
    public void deleteByTopic(final String topic) {
        final Optional<Publication> possiblePublication = publicationRepository.findByTopic(topic);

        if (possiblePublication.isPresent()) {
            publicationRepository.deleteByTopic(topic);
            log.debug(LoggingMessages.DELETED_PUBLICATION, possiblePublication.get().getTopic());
        } else {
            log.debug(LoggingMessages.PUBLICATION_NOT_FOUND_BY_TOPIC, topic);
            throw new IllegalArgumentException(PUBLICATION_NOT_FOUND);
        }
    }
}
