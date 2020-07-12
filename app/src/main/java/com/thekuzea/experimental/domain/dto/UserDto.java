package com.thekuzea.experimental.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;

import com.thekuzea.experimental.domain.dto.validation.NotBlankRequired;
import com.thekuzea.experimental.domain.dto.validation.SizeRequired;

@Value
@Builder
public class UserDto {

    String id;

    @NotBlank(groups = NotBlankRequired.class)
    @Size(min = 3, max = 20, groups = SizeRequired.class)
    String username;

    @NotBlank(groups = NotBlankRequired.class)
    String password;

    @Size(max = 10, groups = SizeRequired.class)
    String role;
}
