package com.thekuzea.experimental.domain.mapper.impl;

import org.springframework.stereotype.Component;

import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.mapper.Mapper;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.util.DateTimeUtil;

@Component
public class PublicationMapper implements Mapper<PublicationDto, Publication> {

    @Override
    public Publication mapToModel(final PublicationDto resource) {
        return Publication.builder()
                .publicationTime(DateTimeUtil.convertStringToOffsetDateTime(resource.getPublicationTime()))
                .topic(resource.getTopic())
                .body(resource.getBody())
                .build();
    }

    @Override
    public PublicationDto mapToDto(final Publication entity) {
        return PublicationDto.builder()
                .id(entity.getId().toString())
                .publishedBy(entity.getPublishedBy().getUsername())
                .publicationTime(DateTimeUtil.convertOffsetDateTimeToString(entity.getPublicationTime()))
                .topic(entity.getTopic())
                .body(entity.getBody())
                .build();
    }
}
