package com.thekuzea.experimental.config.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.thekuzea.experimental.constant.messages.entity.PublicationMessages;
import com.thekuzea.experimental.service.PublicationService;
import com.thekuzea.experimental.util.PublicationTestDataGenerator;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class PublicationServiceMockConfig {

    @Bean
    @Primary
    public PublicationService publicationService() {
        final PublicationService publicationService = mock(PublicationService.class);

        when(publicationService.getAllPublications()).thenReturn(PublicationTestDataGenerator.createPublicationResourceList());
        when(publicationService.getAllPublicationsForCurrentUser()).thenReturn(PublicationTestDataGenerator.createPublicationResourceListForCurrentUser());
        doThrow(new IllegalArgumentException(PublicationMessages.PUBLICATION_NOT_FOUND)).when(publicationService).deleteByTopic("Collinsia heterophylla");

        when(publicationService.addNewPublication(refEq(PublicationTestDataGenerator.createPublicationResourceAsNewPublication())))
                .thenReturn(PublicationTestDataGenerator.createPublicationResourceAsNewPublication());
        when(publicationService.addNewPublication(refEq(PublicationTestDataGenerator.createPublicationResourceAsNewPublicationWhereItAlreadyExists())))
                .thenThrow(new IllegalArgumentException(PublicationMessages.PUBLICATION_ALREADY_EXISTS));
        when(publicationService.addNewPublication(refEq(PublicationTestDataGenerator.createPublicationResourceAsNewPublicationWrongPublisher())))
                .thenThrow(new IllegalArgumentException(PublicationMessages.WRONG_PUBLISHER_PROVIDED));

        return publicationService;
    }
}
