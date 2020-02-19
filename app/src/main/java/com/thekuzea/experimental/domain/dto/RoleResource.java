package com.thekuzea.experimental.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource {

    private String id;

    private String name;

    @Builder.Default
    private List<String> validationMessages = new ArrayList<>();
}
