package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.domain.dto.PublicationDto;

public interface PublicationService {

    List<PublicationDto> getAllPublications();

    List<PublicationDto> getAllPublicationsForCurrentUser();

    PublicationDto addNewPublication(PublicationDto publicationDto);

    void deleteByTopic(String topic);
}
