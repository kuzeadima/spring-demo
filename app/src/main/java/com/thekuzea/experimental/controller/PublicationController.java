package com.thekuzea.experimental.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thekuzea.experimental.domain.dto.PublicationDto;

@RequestMapping("/publication")
public interface PublicationController {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<List<PublicationDto>> getAllPublications();

    @GetMapping(
            value = "/current-user-publications",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<List<PublicationDto>> getAllPublicationsForCurrentUser();

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<PublicationDto> publish(@Valid @RequestBody PublicationDto publicationDto);

    @DeleteMapping("/{topic}")
    ResponseEntity<Void> deleteByTopic(@PathVariable("topic") String topic);
}
