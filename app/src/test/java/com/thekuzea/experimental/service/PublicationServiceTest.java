package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import com.thekuzea.experimental.constant.messages.entity.PublicationMessages;
import com.thekuzea.experimental.domain.dao.PublicationRepository;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.mapper.impl.PublicationMapper;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.service.impl.DefaultPublicationService;
import com.thekuzea.experimental.util.AuthenticationTestDataGenerator;
import com.thekuzea.experimental.util.PublicationTestDataGenerator;
import com.thekuzea.experimental.util.UserTestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PublicationServiceTest {

    private PublicationService publicationService;

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PublicationMapper publicationMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        publicationService = new DefaultPublicationService(publicationRepository, userRepository, publicationMapper, authenticationService);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(publicationRepository, userRepository, publicationMapper, authenticationService);
    }

    @Test
    public void shouldGetAllPublications() {
        final List<Publication> publicationList = PublicationTestDataGenerator.createPublicationList();
        when(publicationRepository.findAll()).thenReturn(publicationList);

        final List<PublicationDto> actualResourceList = publicationService.getAllPublications();

        verify(publicationRepository).findAll();
        verify(publicationMapper, times(3)).mapToDto(any(Publication.class));
        assertThat(actualResourceList).hasSize(3);
    }

    @Test
    public void shouldNotGetAllPublications() {
        final List<Publication> publicationList = Collections.emptyList();
        when(publicationRepository.findAll()).thenReturn(publicationList);

        final List<PublicationDto> actualResourceList = publicationService.getAllPublications();

        verify(publicationRepository).findAll();
        verify(publicationMapper, times(0)).mapToDto(any(Publication.class));
        assertThat(actualResourceList).isEmpty();
    }

    @Test
    public void shouldGetAllPublicationsForCurrentUser() {
        final User user = UserTestDataGenerator.createUser();
        final UserDetails userDetails = AuthenticationTestDataGenerator.createUserDetailsForSimpleUser();
        final List<Publication> publicationList = PublicationTestDataGenerator.createPublicationList();
        when(authenticationService.getCurrentLoggedInUser()).thenReturn(userDetails);
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(publicationRepository.findAllByPublishedBy(any(User.class))).thenReturn(publicationList);

        final List<PublicationDto> actualPublicationDtos = publicationService.getAllPublicationsForCurrentUser();

        verify(authenticationService).getCurrentLoggedInUser();
        verify(userRepository).findByUsername(userDetails.getUsername());
        verify(publicationRepository).findAllByPublishedBy(any(User.class));
        verify(publicationMapper, times(3)).mapToDto(any(Publication.class));
        assertThat(actualPublicationDtos).hasSize(3);
    }

    @Test
    public void shouldNotGetAllPublicationsForCurrentUser() {
        final User user = UserTestDataGenerator.createUser();
        final UserDetails userDetails = AuthenticationTestDataGenerator.createUserDetailsForSimpleUser();
        final List<Publication> publicationList = Collections.emptyList();
        when(authenticationService.getCurrentLoggedInUser()).thenReturn(userDetails);
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(publicationRepository.findAllByPublishedBy(any(User.class))).thenReturn(publicationList);

        final List<PublicationDto> actualPublicationDtos = publicationService.getAllPublicationsForCurrentUser();

        verify(authenticationService).getCurrentLoggedInUser();
        verify(userRepository).findByUsername(userDetails.getUsername());
        verify(publicationRepository).findAllByPublishedBy(any(User.class));
        verify(publicationMapper, times(0)).mapToDto(any(Publication.class));
        assertThat(actualPublicationDtos).isEmpty();
    }

    @Test
    public void shouldAddNewPublication() {
        final PublicationDto expectedPublicationDto = PublicationTestDataGenerator.createPublicationResource();
        final Publication publication = PublicationTestDataGenerator.createPublication();
        final User publishedBy = publication.getPublishedBy();
        when(publicationRepository.existsByTopic(expectedPublicationDto.getTopic())).thenReturn(false);
        when(authenticationService.getCurrentLoggedInUser()).thenReturn(AuthenticationTestDataGenerator.createUserDetailsForSimpleUser());
        when(userRepository.findByUsername(expectedPublicationDto.getPublishedBy())).thenReturn(Optional.of(publishedBy));
        when(publicationMapper.mapToModel(expectedPublicationDto)).thenReturn(publication);

        final PublicationDto actualPublicationDto = publicationService.addNewPublication(expectedPublicationDto);

        verify(publicationRepository).existsByTopic(expectedPublicationDto.getTopic());
        verify(authenticationService).getCurrentLoggedInUser();
        verify(userRepository).findByUsername(expectedPublicationDto.getPublishedBy());
        verify(publicationMapper).mapToModel(expectedPublicationDto);
        verify(publicationRepository).save(any(Publication.class));
        assertThat(actualPublicationDto).isEqualToComparingFieldByField(expectedPublicationDto);
    }

    @Test
    public void shouldNotAddNewPublicationTopicAlreadyExists() {
        final PublicationDto publicationDto = PublicationTestDataGenerator.createPublicationResource();
        when(publicationRepository.existsByTopic(publicationDto.getTopic())).thenReturn(true);

        assertThatThrownBy(() -> publicationService.addNewPublication(publicationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PublicationMessages.PUBLICATION_ALREADY_EXISTS);

        verify(publicationRepository).existsByTopic(publicationDto.getTopic());
    }

    @Test
    public void shouldNotAddNewPublicationWrongPublisher() {
        final PublicationDto publicationDto = PublicationTestDataGenerator.createPublicationResource();
        when(publicationRepository.existsByTopic(publicationDto.getTopic())).thenReturn(false);
        when(authenticationService.getCurrentLoggedInUser()).thenReturn(AuthenticationTestDataGenerator.createUserDetailsForSimpleUser());
        when(userRepository.findByUsername(publicationDto.getPublishedBy())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publicationService.addNewPublication(publicationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PublicationMessages.WRONG_PUBLISHER_PROVIDED);

        verify(publicationRepository).existsByTopic(publicationDto.getTopic());
        verify(authenticationService).getCurrentLoggedInUser();
        verify(userRepository).findByUsername(publicationDto.getPublishedBy());
    }

    @Test
    public void shouldDeletePublicationByTopic() {
        final Publication publication = PublicationTestDataGenerator.createPublication();
        final String topic = publication.getTopic();
        when(publicationRepository.findByTopic(topic)).thenReturn(Optional.of(publication));

        publicationService.deleteByTopic(topic);

        verify(publicationRepository).findByTopic(topic);
        verify(publicationRepository).deleteByTopic(topic);
    }

    @Test
    public void shouldNotDeletePublicationByTopic() {
        final String topic = "unknown";
        when(publicationRepository.findByTopic(topic)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publicationService.deleteByTopic(topic))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PublicationMessages.PUBLICATION_NOT_FOUND);

        verify(publicationRepository).findByTopic(topic);
    }
}
