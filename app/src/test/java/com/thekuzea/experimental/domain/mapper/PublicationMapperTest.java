package com.thekuzea.experimental.domain.mapper;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.mapper.impl.PublicationMapper;
import com.thekuzea.experimental.domain.model.Publication;

import static com.thekuzea.experimental.util.PublicationTestDataGenerator.createPublication;
import static com.thekuzea.experimental.util.PublicationTestDataGenerator.createPublicationResource;

@RunWith(MockitoJUnitRunner.class)
public class PublicationMapperTest {

    private PublicationMapper publicationMapper;

    @Before
    public void setUp() {
        publicationMapper = new PublicationMapper();
    }

    @Test
    public void shouldMapDtoToModel() {
        final PublicationDto publicationDto = createPublicationResource();
        final Publication expectedPublicationModel = createPublication();

        final Publication actualPublicationModel = publicationMapper.mapToModel(publicationDto);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualPublicationModel.getPublicationTime()).isEqualTo(expectedPublicationModel.getPublicationTime());
            softly.assertThat(actualPublicationModel.getTopic()).isEqualTo(expectedPublicationModel.getTopic());
            softly.assertThat(actualPublicationModel.getBody()).isEqualTo(expectedPublicationModel.getBody());
        });
    }

    @Test
    public void shouldMapModelToDto() {
        final PublicationDto expectedPublicationDto = createPublicationResource();
        final Publication publicationModel = createPublication();

        final PublicationDto actualPublicationDto = publicationMapper.mapToDto(publicationModel);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(actualPublicationDto.getId()).isEqualTo(expectedPublicationDto.getId());
            softly.assertThat(actualPublicationDto.getPublishedBy()).isEqualTo(expectedPublicationDto.getPublishedBy());
            softly.assertThat(actualPublicationDto.getPublicationTime()).isEqualTo(expectedPublicationDto.getPublicationTime());
            softly.assertThat(actualPublicationDto.getTopic()).isEqualTo(expectedPublicationDto.getTopic());
            softly.assertThat(actualPublicationDto.getBody()).isEqualTo(expectedPublicationDto.getBody());
        });
    }
}
