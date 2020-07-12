package com.thekuzea.experimental.controller.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.controller.PublicationController;
import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.service.PublicationService;

@RestController
@RequiredArgsConstructor
public class DefaultPublicationController implements PublicationController {

    private final PublicationService publicationService;

    @Override
    public ResponseEntity<List<PublicationDto>> getAllPublications() {
        return ResponseEntity.ok(publicationService.getAllPublications());
    }

    @Override
    public ResponseEntity<List<PublicationDto>> getAllPublicationsForCurrentUser() {
        return ResponseEntity.ok(publicationService.getAllPublicationsForCurrentUser());
    }

    @Override
    public ResponseEntity<PublicationDto> publish(final PublicationDto incomingResource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.addNewPublication(incomingResource));
    }

    @Override
    public ResponseEntity<Void> deleteByTopic(final String topic) {
        publicationService.deleteByTopic(topic);
        return ResponseEntity.accepted().build();
    }
}
