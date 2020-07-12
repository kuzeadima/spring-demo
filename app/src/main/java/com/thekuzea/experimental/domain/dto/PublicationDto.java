package com.thekuzea.experimental.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PublicationDto {

    String id;

    String publishedBy;

    @NotBlank
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[+]\\d{2}:\\d{2}$",
            message = "{validation.constraints.WrongDate.message}"
    )
    String publicationTime;

    @NotBlank
    @Size(min = 10, max = 80)
    String topic;

    @NotBlank
    @Size(min = 30)
    String body;
}
