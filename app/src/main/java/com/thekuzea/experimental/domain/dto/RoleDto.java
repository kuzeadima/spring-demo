package com.thekuzea.experimental.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoleDto {

    String id;

    @NotBlank
    @Size(min = 2, max = 10)
    String name;
}
